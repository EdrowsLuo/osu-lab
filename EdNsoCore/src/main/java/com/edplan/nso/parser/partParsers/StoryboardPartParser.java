package com.edplan.nso.parser.partParsers;

import com.edplan.framework.math.Vec2;
import com.edplan.nso.NsoException;
import com.edplan.nso.filepart.PartEvents;
import com.edplan.nso.filepart.PartVariables;
import com.edplan.nso.storyboard.Storyboard;
import com.edplan.nso.storyboard.elements.CommandTimeLineGroup;
import com.edplan.nso.storyboard.elements.StoryboardSprite;
import com.edplan.superutils.U;
import com.edplan.nso.storyboard.elements.StoryboardAnimation;
import com.edplan.nso.storyboard.elements.StoryboardSample;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.nso.ParsingBeatmap;

public class StoryboardPartParser extends PartParser<PartEvents> {
    private Storyboard storyboard = new Storyboard();

    public VariablesDecoder variableDecoder;

    private PartVariables variables;

    private PartEvents partEvents = new PartEvents();

    private StoryboardSprite storyboardSprite;

    private CommandTimeLineGroup timelineGroup;

    private ParsingBeatmap parsingData;

    private boolean enableRawData = false;

    public StoryboardPartParser(ParsingBeatmap data) {
        variableDecoder = new VariablesDecoder();
        variables = variableDecoder.getPart();
        this.parsingData = data;
    }

    public Storyboard getStoryboard() {
        return storyboard;
    }

    private boolean handleEvents(String l, int depth) throws NsoException {
        String[] spl = l.split(",");
        if (depth == 0) {
            storyboardSprite = null;
            parsedObjects++;
            Storyboard.EventObjType eventType = Storyboard.EventObjType.parse(spl[0]);
            if (eventType == null) {
                setErrMessage("unknow eventType: " + spl[0]);
                return false;
            }
            switch (eventType) {
                case Sprite: {
                    Storyboard.Layer layer = parseLayer(spl[1]);
                    Storyboard.Origin origin = parseOrigin(spl[2]);
                    String path = cleanFilename(spl[3]);
                    float x = Float.parseFloat(spl[4]);
                    float y = Float.parseFloat(spl[5]);
                    storyboardSprite = new StoryboardSprite(path, origin.getAnchor(), new Vec2(x, y));
                    storyboard.getLayer(layer.name()).add(storyboardSprite);

                    if (enableRawData) storyboardSprite.rawData.append(l).append("\n");
                }
                break;
                case Animation: {
                    Storyboard.Layer layer = parseLayer(spl[1]);
                    Storyboard.Origin origin = parseOrigin(spl[2]);
                    String path = cleanFilename(spl[3]);
                    float x = Float.parseFloat(spl[4]);
                    float y = Float.parseFloat(spl[5]);
                    int frameCount = Integer.parseInt(spl[6]);
                    double frameDelay = Double.parseDouble(spl[7]);
                    Storyboard.AnimationLoopType loopType = (spl.length > 8) ? Storyboard.AnimationLoopType.valueOf(spl[8]) : Storyboard.AnimationLoopType.LoopForever;
                    storyboardSprite = new StoryboardAnimation(path, origin.getAnchor(), new Vec2(x, y), frameCount, frameDelay, loopType);
                    storyboard.getLayer(layer.name()).add(storyboardSprite);

                    if (enableRawData) storyboardSprite.rawData.append(l).append("\n");
                }
                break;
                case Sample: {
                    double time = Double.parseDouble(spl[1]);
                    Storyboard.Layer layer = parseLayer(spl[2]);
                    String path = cleanFilename(spl[3]);
                    float volume = spl.length > 4 ? Float.parseFloat(spl[4]) : 100;
                    storyboard.getLayer(layer.name()).add(new StoryboardSample(path, time, volume));
                }
                break;
            }
        } else {
            if (storyboardSprite != null) storyboardSprite.rawData.append(l).append("\n");

            if (depth < 2) {
                timelineGroup = ((storyboardSprite != null) ? storyboardSprite.getCommandTimeLineGroup() : null);
            }
            String commandType = spl[0];
            switch (commandType) {
                case "T": {
                    String triggerName = spl[1];
                    double startTime = spl.length > 2 ? Double.parseDouble(spl[2]) : Double.MIN_VALUE;
                    double endTime = spl.length > 3 ? Double.parseDouble(spl[3]) : Double.MAX_VALUE;
                    int groupNumber = spl.length > 4 ? Integer.parseInt(spl[4]) : 0;
                    timelineGroup = (storyboardSprite != null) ? storyboardSprite.addTrigger(triggerName, startTime, endTime, groupNumber) : null;
                }
                break;
                case "L": {
                    double startTime = Double.parseDouble(spl[1]);
                    int loopCount = Integer.parseInt(spl[2]);
                    timelineGroup = (storyboardSprite != null) ? storyboardSprite.addLoop(startTime, loopCount) : null;
                }
                break;
                default: {
                    if (spl[3] == null || spl[3].isEmpty()) {
                        spl[3] = spl[2];
                    }
                    Easing easing = Easing.values()[Integer.parseInt(spl[1])];
                    double startTime = Double.parseDouble(spl[2]);
                    double endTime = Double.parseDouble(spl[3]);

                    switch (commandType) {
                        case "F": {
                            float startValue = Float.parseFloat(spl[4]);
                            float endValue = (spl.length > 5) ? Float.parseFloat(spl[5]) : startValue;
                            if (timelineGroup != null)
                                timelineGroup.Alpha.add(easing, startTime, endTime, startValue, endValue);
                        }
                        break;
                        case "S": {
                            float startValue = Float.parseFloat(spl[4]);
                            float endValue = (spl.length > 5) ? Float.parseFloat(spl[5]) : startValue;
                            if (timelineGroup != null) {
                                timelineGroup.ScaleX.add(easing, startTime, endTime, startValue, endValue);
                                timelineGroup.ScaleY.add(easing, startTime, endTime, startValue, endValue);
                            }
                        }
                        break;
                        case "V": {
                            float startX = Float.parseFloat(spl[4]);
                            float startY = Float.parseFloat(spl[5]);
                            float endX = (spl.length > 6) ? Float.parseFloat(spl[6]) : startX;
                            float endY = (spl.length > 7) ? Float.parseFloat(spl[7]) : startY;
                            if (timelineGroup != null) {
                                timelineGroup.ScaleX.add(easing, startTime, endTime, startX, endX);
                                timelineGroup.ScaleY.add(easing, startTime, endTime, startY, endY);
                            }
                        }
                        break;
                        case "R": {
                            float startValue = Float.parseFloat(spl[4]);
                            float endValue = (spl.length > 5) ? Float.parseFloat(spl[5]) : startValue;
                            if (timelineGroup != null)
                                timelineGroup.Rotation.add(easing, startTime, endTime, startValue, endValue);
                        }
                        break;
                        case "M": {
                            float startX = Float.parseFloat(spl[4]);
                            float startY = Float.parseFloat(spl[5]);
                            float endX = (spl.length > 6) ? Float.parseFloat(spl[6]) : startX;
                            float endY = (spl.length > 7) ? Float.parseFloat(spl[7]) : startY;
                            if (timelineGroup != null) {
                                timelineGroup.X.add(easing, startTime, endTime, startX, endX);
                                timelineGroup.Y.add(easing, startTime, endTime, startY, endY);
                            }
                        }
                        break;
                        case "MX": {
                            float startValue = Float.parseFloat(spl[4]);
                            float endValue = (spl.length > 5) ? Float.parseFloat(spl[5]) : startValue;
                            if (timelineGroup != null)
                                timelineGroup.X.add(easing, startTime, endTime, startValue, endValue);
                        }
                        break;
                        case "MY": {
                            float startValue = Float.parseFloat(spl[4]);
                            float endValue = (spl.length > 5) ? Float.parseFloat(spl[5]) : startValue;
                            if (timelineGroup != null)
                                timelineGroup.Y.add(easing, startTime, endTime, startValue, endValue);
                        }
                        break;
                        case "C": {
                            float startR = Float.parseFloat(spl[4]);
                            float startG = Float.parseFloat(spl[5]);
                            float startB = Float.parseFloat(spl[6]);
                            float endR = (spl.length > 7) ? Float.parseFloat(spl[7]) : startR;
                            float endG = (spl.length > 8) ? Float.parseFloat(spl[8]) : startG;
                            float endB = (spl.length > 9) ? Float.parseFloat(spl[9]) : startB;
                            if (timelineGroup != null)
                                timelineGroup.Colour.add(easing, startTime, endTime, Color4.rgb255(startR, startG, startB), Color4.rgb255(endR, endG, endB));
                        }
                        break;
                        case "P": {
                            String type = spl[4];
                            switch (type) {
                                case "A":
                                    if (timelineGroup != null) {
                                        timelineGroup.BlendingMode.add(easing, startTime, endTime, BlendType.Additive, (startTime == endTime ? BlendType.Additive : BlendType.Normal));
                                    }
                                    break;
                                case "H":
                                    if (timelineGroup != null) {
                                        timelineGroup.FlipH.add(easing, startTime, endTime, true, startTime == endTime);
                                    }
                                    break;
                                case "V":
                                    if (timelineGroup != null) {
                                        timelineGroup.FlipV.add(easing, startTime, endTime, true, startTime == endTime);
                                    }
                                    break;
                            }
                        }
                        break;
                        default:
                            throw new NsoException("unknow command type: " + commandType);
                    }
                }
            }
        }
        return true;
    }

    private Storyboard.Layer parseLayer(String value) {
        return Storyboard.Layer.parse(value);
    }

    private Storyboard.Origin parseOrigin(String value) {
        return Storyboard.Origin.parse(value);
    }

    private String cleanFilename(String s) throws NsoException {
        if (s.length() >= 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            return s.substring(1, s.length() - 1).replace('\\', '/');
        } else {
            //setErrMessage("err filename : "+s);
            throw new NsoException("err filename : " + s);
        }
    }

    private static final int BLOCK_LOOP_COUNT = 20;

    int parsedObjects = 0;

    @Override
    public boolean parse(String l) throws NsoException {


        //if(parsingData.getParsingLineIndex()==172240)parsedObjects=0;
		/*
		if(parsingData.getParsingLineIndex()<161204||parsedObjects>=2){
			return true;
		}
		*/
		/*else if(parsingData.getParsingLineIndex()==172087){
			System.out.println(l);
			throw new RuntimeException("停!");
		}*/
        if (l.trim().isEmpty() || l.startsWith("//")) {
            //在Storyboard部分，支持//注释方法（仅限于以//开头的行）
            return true;
        } else {
            int depth = 0;
            while (l.charAt(0) == ' ' || l.charAt(0) == '_') {
                depth++;
                l = l.substring(1);
            }
            //支持使用变量
            int loops = 0;
            while (l.indexOf('$') >= 0) {
                String origin = l;
                String[] spl = l.split("'");
                for (int i = 0; i < spl.length; i++) {
                    String item = spl[i];
                    if (item.startsWith("$") && (item = variables.getVariable(item)) != null) {
                        spl[i] = item;
                    }
                }
                l = U.join(spl, ",");
                if (l.equals(origin)) {
                    break;
                }
                loops++;
                if (loops > BLOCK_LOOP_COUNT) {
                    setErrMessage("MAYBE A ENDLESS LOOP! loops=" + loops);
                    return false;
                }
            }
            return handleEvents(l, depth);
        }
    }

    @Override
    public PartEvents getPart() {

        return partEvents;
    }

    public static class VariablesDecoder extends PartParser<PartVariables> {
        private PartVariables part = new PartVariables();

        @Override
        public boolean parse(String l) throws NsoException {

            String[] spl = U.divide(l, l.indexOf('='));
            if (spl == null) {
                return false;
            }
            part.addVariable(spl[0], spl[1]);
            return true;
        }

        @Override
        public PartVariables getPart() {

            return part;
        }
    }
}
