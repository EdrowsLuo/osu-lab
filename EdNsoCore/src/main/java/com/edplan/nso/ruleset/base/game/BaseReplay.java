package com.edplan.nso.ruleset.base.game;

import com.edplan.framework.utils.io.MemoryFile;
import com.edplan.nso.ruleset.base.game.judge.JudgeData;
import com.edplan.nso.ruleset.base.game.judge.JudgeNode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 数据存储格式说明：
 * 分为若干个frame，每个frame里
 */
public class BaseReplay {

    public static final int FRAME_START_CHECKER = 291891928;

    public static final int FRAME_END_CHECKER = 712812631;

    private final JudgeNode[] judgeNodes;

    private final MemoryFile memoryFile = new MemoryFile(1024 * 16);

    private DataOutputStream dataOutputStream = new DataOutputStream(memoryFile.getOutputStream());

    private DataInputStream dataInputStream = new DataInputStream(memoryFile.getInputStream());

    private double savedFrameTime = -999999999;

    private boolean frameIsHandled = true;

    public BaseReplay(JudgeNode[] judgeNodes) {
        this.judgeNodes = Arrays.copyOf(judgeNodes, judgeNodes.length);
    }

    /**
     * 检查原始输入事件，如果需要更新，将新的一帧数据写进output里
     * 播放replay的时候无需调用这个方法
     * @param frameTime 当前判定帧的时间
     */
    public void writeJudgeEvents(double frameTime) {
        boolean needUpdate = false;
        for (JudgeNode node : judgeNodes) {
            if (node.updater.needUpdate()) {
                needUpdate = true;
                break;
            }
        }
        if (needUpdate) {
            //当需要更新的时候写入一帧事件
            try {
                dataOutputStream.writeInt(FRAME_START_CHECKER);//用于检查格式，看是否是一帧的开始
                dataOutputStream.writeDouble(frameTime);
                for (JudgeNode node : judgeNodes) {
                    if (node.updater.needUpdate()) {
                        dataOutputStream.writeInt(JudgeData.USING_KEYFRAME_DATA);
                        node.updater.update(dataOutputStream);
                    } else {
                        dataOutputStream.writeInt(JudgeData.AUTO_CHANGE);
                    }
                }
                dataOutputStream.writeInt(FRAME_END_CHECKER);//用于检查格式，看是否是一帧的结束
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从replay的输入流里更新当前状态
     * 无论是auto还是正常游戏均通过这个方法更新
     * @param frameTime
     */
    public void updateJudgeData(double frameTime) {
        try {
            if (!frameIsHandled) {
                //当前帧数据还没有被处理
                handleCurrentFrame(frameTime);
            } else {
                //当前帧被处理完了，准备读取下一帧数据
                if (dataInputStream.available() > 0) {
                    if (dataInputStream.readInt() != FRAME_START_CHECKER) {
                        throw new RuntimeException("err replay format at FRAME_START_CHECKER");
                    }
                    savedFrameTime = dataInputStream.readDouble();
                    frameIsHandled = false;
                    handleCurrentFrame(frameTime);
                }else {
                    //还没有数据写入，自动更新
                    normalUpdate();
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCurrentFrame(double frameTime) throws IOException {
        if (frameTime >= savedFrameTime) {
            //保存的帧时间小于当前时间，将这帧数据处理掉
            for (JudgeNode node : judgeNodes) {
                int type = dataInputStream.readInt();
                switch (type) {
                    case JudgeData.AUTO_CHANGE: {
                        node.data.update(JudgeData.AUTO_CHANGE, null);
                    }
                    break;
                    case JudgeData.USING_KEYFRAME_DATA: {
                        node.data.update(JudgeData.USING_KEYFRAME_DATA, dataInputStream);
                    }
                    break;
                    default:
                        throw new RuntimeException("err replay format at UPDATE_TYPE");
                }
            }
            if (dataInputStream.readInt() != FRAME_END_CHECKER) {
                throw new RuntimeException("err replay format at FRAME_END_CHECKER");
            }
            frameIsHandled = true;
        } else {
            //下一帧时间还没有到
            normalUpdate();
        }
    }

    private void normalUpdate() {
        for (JudgeNode node : judgeNodes) {
            node.data.update(JudgeData.AUTO_CHANGE, null);
        }
    }

}
