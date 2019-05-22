package com.edlplan.nso.ruleset.base.game.judge;

import com.edlplan.framework.timing.FrameClock;
import com.edlplan.framework.ui.inputs.EdKeyEvent;
import com.edlplan.framework.ui.inputs.EdMotionEvent;
import com.edlplan.framework.utils.interfaces.Consumer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 光标数据
 *
 * 帧格式为
 *
 * TAG    short
 * SIZE   byte
 * block(count = SIZE){
 *      TYPE & ID   byte
 *         X        float
 *         Y        float
 *       TIME       double
 * }
 *
 */
public class CursorData extends JudgeData {

    public static final int SUB_STATE = 0;

    public static final int SUB_ACTION = 1;

    public static final short TAG = 1234; //用于数据校验

    public static final byte TYPE_MASK = 3 << 5;

    public static final byte ID_MASK = (1 << 5) - 1;

    public static final byte TYPE_DOWN = 1 << 5;

    public static final byte TYPE_UP = 2 << 5;

    public static final byte TYPE_MOVE = 3 << 5;

    public static final int MAX_CURSOR_COUNT = 10;

    private CursorDataUpdater updater = new CursorDataUpdater();

    private CursorHolder[] cursors = new CursorHolder[MAX_CURSOR_COUNT];
    private CursorHolder[] hasAction = new CursorHolder[MAX_CURSOR_COUNT];

    private int hasActionCount = 0;

    private int actions = 0;

    public CursorData() {
        for (int i=0;i<cursors.length;i++) {
            cursors[i] = new CursorHolder(i);
        }
    }

    public boolean hasMoreAction() {
        return actions != 0;
    }

    public CursorHolder[] getCursors() {
        return cursors;
    }

    private void autoChange() {
        for (CursorHolder holder : cursors) {
            holder.down.reset();
            holder.up.reset();
        }
        actions = 0;
    }

    private void usingKeyFrameData(DataInput inputStream) {
        try {
            autoChange();
            if (TAG != inputStream.readShort()) {
                throw new RuntimeException("bad stream format");
            }
            int size = inputStream.readByte();
            while (size > 0) {
                size--;
                byte type = inputStream.readByte();
                int id = type & ID_MASK;
                int htype = type & TYPE_MASK;
                CursorHolder holder = cursors[id];
                holder.x = inputStream.readFloat();
                holder.y = inputStream.readFloat();
                double time = inputStream.readDouble();
                holder.time = time;
                switch (htype) {
                    case TYPE_DOWN:
                        holder.down.add();
                        holder.down.time = time;
                        holder.isDown = true;
                        break;
                    case TYPE_MOVE:
                        holder.isDown = true;
                        break;
                    case TYPE_UP:
                        holder.up.add();
                        holder.up.time = time;
                        holder.isDown = false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 遍历所有点击、保留在屏幕以及发生离开屏幕事件的Cursor
     */
    public void forEachActionCursor(Consumer<CursorHolder> consumer) {
        for (int i = 0; i < hasActionCount; i++) {
            consumer.consume(hasAction[i]);
        }
    }

    @Override
    public void update(int type, DataInput inputStream) {
        switch (type) {
            case AUTO_CHANGE:
                autoChange();
                break;
            case USING_KEYFRAME_DATA:
                usingKeyFrameData(inputStream);
                break;
        }
        hasActionCount = 0;
        for (int i = 0; i < MAX_CURSOR_COUNT; i++) {
            if (cursors[i].isDown || (!(cursors[i].down.empty() || cursors[i].up.empty()))) {
                hasAction[hasActionCount++] = cursors[i];
            }
        }
    }

    @Override
    public JudgeDataUpdater getDataUpdater() {
        return updater;
    }

    @Override
    public int subTypesCount() {
        return 2;
    }

    public class CursorHolder {

        public boolean isDown = false;

        public double time;

        public float x, y;

        public final int id;

        public final CursorSpEvent down = new CursorSpEvent();

        public final CursorSpEvent up = new CursorSpEvent();

        public CursorHolder(int id) {
            this.id = id;
        }
    }

    public class CursorSpEvent {

        int idx = -1;

        public double time;

        public void reset() {
            idx = -1;
        }

        public boolean empty() {
            return idx == -1;
        }

        public void add() {
            actions++;
            idx++;
        }

        public void consume() {
            if (idx != -1) {
                actions--;
                idx--;
            }
        }
    }

    public static class CursorDataUpdater extends JudgeDataUpdater {

        private final LinkedList<EdMotionEvent> events = new LinkedList<>();
        
        @Override
        public boolean needUpdate() {
            return events.size() > 0;
        }

        @Override
        public boolean update(DataOutput outputStream) {
            synchronized (events) {
                if (events.size() > 0) {
                    try {
                        final int size = events.size();
                        outputStream.writeShort(TAG);
                        outputStream.writeByte(size);
                        for (int i = 0; i < size; i++) {
                            EdMotionEvent event = events.removeFirst();
                            byte type = (byte) event.getPointerId();
                            switch (event.getEventType()) {
                                case Down:
                                    type |= TYPE_DOWN;
                                    break;
                                case Up:
                                    type |= TYPE_UP;
                                    break;
                                case Move:
                                    type |= TYPE_MOVE;
                                    break;
                            }
                            outputStream.writeByte(type);
                            outputStream.writeFloat(event.eventPosition.x);
                            outputStream.writeFloat(event.eventPosition.y);
                            outputStream.writeDouble(event.time);
                        }
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean onMotionEvent(EdMotionEvent... event) {
            synchronized (events) {
                for (EdMotionEvent e : event) {
                    events.add(e);
                }
            }
            return true;
        }

        @Override
        public boolean onKeyEvent(EdKeyEvent event) {
            return false;
        }
    }
}
