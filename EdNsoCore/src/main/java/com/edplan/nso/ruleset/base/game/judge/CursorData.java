package com.edplan.nso.ruleset.base.game.judge;

import com.edplan.framework.timing.FrameClock;
import com.edplan.framework.ui.inputs.EdKeyEvent;
import com.edplan.framework.ui.inputs.EdMotionEvent;

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

    public static final short TAG = 1234; //用于数据校验

    public static final byte TYPE_MASK = 3 << 5;

    public static final byte ID_MASK = (1 << 5) - 1;

    public static final byte TYPE_DOWN = 1 << 5;

    public static final byte TYPE_UP = 2 << 5;

    public static final byte TYPE_MOVE = 3 << 5;

    public static final int MAX_CURSOR_COUNT = 10;

    private CursorDataUpdater updater = new CursorDataUpdater();

    private CursorHolder[] cursors = new CursorHolder[MAX_CURSOR_COUNT];

    public CursorData() {
        for (int i=0;i<cursors.length;i++) {
            cursors[i] = new CursorHolder(i);
        }
    }

    public CursorHolder[] getCursors() {
        return cursors;
    }

    private void autoChange() {
        for (CursorHolder holder : cursors) {
            holder.down.reset();
            holder.up.reset();
        }
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
                switch (htype) {
                    case TYPE_DOWN:
                        holder.down.add(time);
                        holder.isDown = true;
                        break;
                    case TYPE_MOVE:
                        holder.isDown = true;
                        break;
                    case TYPE_UP:
                        holder.up.add(time);
                        holder.isDown = false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    }

    @Override
    public JudgeDataUpdater getDataUpdater() {
        return updater;
    }

    public static class CursorHolder {

        public boolean isDown = false;

        public float x, y;

        public final int id;

        public final CursorSpEvent down = new CursorSpEvent();

        public final CursorSpEvent up = new CursorSpEvent();

        public CursorHolder(int id) {
            this.id = id;
        }
    }

    public static class CursorSpEvent {

        private double[] buffer = new double[10];

        int idx = -1;

        public void reset() {
            idx = -1;
        }

        public boolean empty() {
            return idx == -1;
        }

        public void add(double time) {
            buffer[++idx] = time;
        }

        public double time() {
            return buffer[idx];
        }

        public void consume() {
            if (idx != -1) {
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
