package com.edplan.nso.ruleset.base.object;

import com.edplan.framework.ui.inputs.EdKeyEvent;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.nso.ruleset.base.game.World;
import com.edplan.nso.ruleset.base.game.judge.JudgeData;
import com.edplan.nso.ruleset.base.game.judge.JudgeDataUpdater;
import com.edplan.nso.ruleset.base.game.judge.RawInputHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JudgeObjectsManager implements RawInputHandler {

    private ArrayList<JudgeObject> judgeObjects = new ArrayList<>();

    private LinkedList<JudgeObjectNode> judgeObjectLinkedList = new LinkedList<>();

    private LinkedList<JudgeObjectNode> waitingObjects = new LinkedList<>();

    private LinkedList<JudgeObjectNode> usingObjects = new LinkedList<>();

    private List<JudgeNode> judgeNodes = new ArrayList<>();

    private Class<? extends JudgeData>[] judgeTypes;

    private HashMap<Class<? extends JudgeData>, JudgeNode> judgeNodeHashMap = new HashMap<>();

    public void preloadJudgeDataType(Class<? extends JudgeData>[] types) {
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
    }


    /**
     * 处理一帧判定
     * @param world 世界上下文
     */
    public void update(World world) {
        final double judgeFrameTime = world.getJudgeFrameTime();
        addNewObjects(judgeFrameTime);
        removeFailedObjects(judgeFrameTime);
        dispatchJudgeDatas(world);
        removeReleasedObjects();
    }

    /**
     * 载入、重载物件
     */
    private void reloadObjects() {
        waitingObjects.clear();
        usingObjects.clear();
        judgeObjectLinkedList.clear();
        Collections.sort(judgeObjects, (a, b) -> (int) Math.signum(a.getStartJudgeTime() - b.getStartJudgeTime()));
        for (JudgeObject object : judgeObjects) {
            judgeObjectLinkedList.add(new JudgeObjectNode(object));
        }
        waitingObjects.addAll(judgeObjectLinkedList);
    }

    /**
     * 处理将要新加入的判定物件
     * @param time 时间戳
     */
    private void addNewObjects(double time) {
        for (JudgeObjectNode object : waitingObjects) {
            if (object.object.getStartJudgeTime() <= time) {
                usingObjects.addLast(object);
            } else {
                break;
            }
        }
    }

    /**
     * 处理超时判定物件
     * @param time 时间戳
     */
    private void removeFailedObjects(double time) {
        Iterator<JudgeObjectNode> iterator = usingObjects.iterator();
        while (iterator.hasNext()) {
            final JudgeObject object = iterator.next().object;
            if (object.getJudgeFailedTime() <= time) {
                object.releaseObject();
                iterator.remove();
            }
        }
    }

    /**
     * 分发事件
     * @param world
     */
    private void dispatchJudgeDatas(World world) {
        for (JudgeObjectNode objectNode : usingObjects) {
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

    /**
     * 将已经release了的判定物件移除
     */
    public void removeReleasedObjects() {
        Iterator<JudgeObjectNode> iterator = usingObjects.iterator();
        while (iterator.hasNext()) {
            final JudgeObject object = iterator.next().object;
            if (object.isRelease()) {
                iterator.remove();
            }
        }
    }


    @Override
    public boolean onMotionEvent(EdMotionEvent event) {
        for (JudgeNode node : judgeNodes) {
            node.updater.onMotionEvent(event);
        }
        return false;
    }

    @Override
    public boolean onKeyEvent(EdKeyEvent event) {
        for (JudgeNode node : judgeNodes) {
            node.updater.onKeyEvent(event);
        }
        return false;
    }


    public class JudgeNode {

        public final JudgeDataUpdater updater;

        public final JudgeData data;

        public boolean interrupted = false;

        public JudgeNode(JudgeData data) {
            this.data = data;
            this.updater = data.getDataUpdater();
        }

    }

    public class JudgeObjectNode {

        public final JudgeObject object;

        public final JudgeNode[] datas;

        public JudgeObjectNode(JudgeObject object) {
            this.object = object;
            Class<? extends JudgeData>[] listeningDatas = object.getListeningDatas();
            datas = new JudgeNode[listeningDatas.length];
            for (int i = 0; i < datas.length; i++) {
                datas[i] = judgeNodeHashMap.get(listeningDatas[i]);
            }
        }

    }
}
