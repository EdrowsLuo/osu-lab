package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.timing.Schedule;
import com.edplan.framework.ui.inputs.EdKeyEvent;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.utils.advance.ClassifiedList;
import com.edplan.framework.utils.io.MemoryFile;
import com.edplan.nso.ruleset.base.game.BaseReplay;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.JudgeData;
import com.edplan.nso.ruleset.base.game.judge.JudgeDataUpdater;
import com.edplan.nso.ruleset.base.game.judge.JudgeObject;
import com.edplan.nso.ruleset.base.game.judge.RawInputHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 管理判定的类，主要工作在判定线程里
 */
public class JudgeWorld implements RawInputHandler {

    private static final int ACTIVE_OBJECT = 1;

    private ArrayList<JudgeObject> judgeObjects = new ArrayList<>();

    private List<JudgeNode> judgeNodes = new ArrayList<>();

    private Class<? extends JudgeData>[] judgeTypes;

    private HashMap<Class<? extends JudgeData>, JudgeNode> judgeNodeHashMap = new HashMap<>();

    private Schedule activeSchedule = new Schedule();

    private Schedule timeoutSchedule = new Schedule();

    private ClassifiedList<JudgeObjectNode> classifiedList = new ClassifiedList<>();

    private BaseReplay replay;

    public JudgeWorld() {

    }

    public void addJudgeObject(JudgeObject judgeObject) {
        judgeObjects.add(judgeObject);
    }

    @SafeVarargs
    public final void preloadJudgeDataType(Class<? extends JudgeData>... types) {
        judgeTypes = types;
        judgeNodes.clear();
        judgeNodeHashMap.clear();
        for (Class<? extends JudgeData> k : types) {
            try {
                JudgeNode node = new JudgeNode(k.newInstance());
                judgeNodes.add(node);
                judgeNodeHashMap.put(k, node);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        replay = new BaseReplay(judgeNodes);
    }

    /**
     * 处理一帧判定
     * @param world 世界上下文
     */
    public void update(World world) {
        final double judgeFrameTime = world.getJudgeFrameTime();
        activeSchedule.update(judgeFrameTime); //添加新的物件
        timeoutSchedule.update(judgeFrameTime); //删除过时物件

        //更新用户输入
        replay.writeJudgeEvents(world.getJudgeFrameTime(), world);
        replay.updateJudgeData(world.getJudgeFrameTime());

        //分发处理判定事件
        dispatchJudgeDatas(world);

        //移除被释放了的物件
        removeReleasedObjects(judgeFrameTime);
    }

    /**
     * 载入物件
     */
    public void loadObjects() {
        classifiedList.clear();
        for (JudgeObject object : judgeObjects) {
            ClassifiedList<JudgeObjectNode>.Node node = classifiedList.add(new JudgeObjectNode(object));
            node.getObject().node = node;
        }

        activeSchedule.clear();
        timeoutSchedule.clear();
        for (JudgeObjectNode judgeObject : classifiedList) {
            activeSchedule.addEvent(
                    judgeObject.object.getStartJudgeTime(),
                    judgeObject::active
            );

            timeoutSchedule.addEvent(
                    judgeObject.object.getJudgeFailedTime(),
                    () -> {
                        if (judgeObject.isActive()) {
                            if (!judgeObject.object.isRelease()) {
                                judgeObject.object.releaseObject();
                            }
                            judgeObject.remove();
                        }
                    }
            );
        }
    }

    /**
     * 分发事件
     * @param world
     */
    private void dispatchJudgeDatas(World world) {
        for (JudgeObjectNode objectNode : classifiedList.getAll(ACTIVE_OBJECT)) {
            final JudgeObject object = objectNode.object;
            if (objectNode.datas.length == 0) {
                object.handle(null, world);
            } else {
                for (JudgeNode node : objectNode.datas) {
                    if (node.interrupted) {
                        continue;
                    } else {
                        if (object.handle(node.data, world)) {
                            node.interrupted = true;
                        }
                    }
                }
            }
        }
    }

    private void removeReleasedObjects(double time) {
        for (JudgeObjectNode objectNode : classifiedList.getAll(ACTIVE_OBJECT)) {
            if (objectNode.object.isRelease()) {
                objectNode.remove();
            }
        }
    }

    @Override
    public boolean onMotionEvent(EdMotionEvent... event) {
        for (JudgeNode node : judgeNodes) {
            node.updater.onMotionEvent(event);
        }
        return true;
    }

    @Override
    public boolean onKeyEvent(EdKeyEvent event) {
        for (JudgeNode node : judgeNodes) {
            node.updater.onKeyEvent(event);
        }
        return true;
    }

    public class JudgeObjectNode {

        public final JudgeObject object;

        public final JudgeNode[] datas;

        private ClassifiedList<JudgeObjectNode>.Node node;

        public JudgeObjectNode(JudgeObject object) {
            this.object = object;
            Class<? extends JudgeData>[] listeningDatas = object.getListeningDatas();
            datas = new JudgeNode[listeningDatas.length];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = judgeNodeHashMap.get(listeningDatas[i]);
            }
        }

        public boolean isActive() {
            return node.getType() == ACTIVE_OBJECT;
        }

        public void active() {
            node.changeType(ACTIVE_OBJECT);
        }

        public void remove() {
            node.changeType(-1);
        }

    }

}
