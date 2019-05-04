package com.edplan.nso.ruleset.std.objects.v2.direct;

import com.edplan.nso.ruleset.base.game.GameObject;
import com.edplan.nso.ruleset.std.beatmap.StdBeatmap;
import com.edplan.nso.ruleset.std.objects.v2.raw.StdCircle;
import com.edplan.nso.ruleset.std.objects.v2.raw.StdGameObject;
import com.edplan.nso.ruleset.std.objects.v2.raw.StdSlider;
import com.edplan.nso.ruleset.std.objects.v2.raw.StdSpinner;

import java.util.ArrayList;
import java.util.List;

public class DirectStdBeatmap {

    private StdBeatmap beatmap;

    private List<DirectStdGameObject> gameObjects = new ArrayList<>();

    private DirectStdGameObjectFactory factory;

    private StdGameProperty property;

    public DirectStdBeatmap(StdBeatmap beatmap, StdGameProperty property, DirectStdGameObjectFactory factory) {
        this.beatmap = beatmap;
        this.property = property;
        this.factory = factory;
    }

    public void loadObjects() {
        int comboIndex = 0;
        for (GameObject raw : beatmap.getAllHitObjects()) {
            if (raw instanceof StdGameObject) {
                StdGameObject o = (StdGameObject) raw;
                if (o.isNewCombo()) {
                    comboIndex = 0;
                }
                comboIndex++;
                if (o instanceof StdCircle) {
                    gameObjects.add(factory.createCircle((StdCircle) o, comboIndex, beatmap, property));
                } else if (o instanceof StdSlider) {
                    DirectStdSliderBody body = factory.createSliderBody((StdSlider) o, comboIndex, beatmap, property);
                    body.addNestedObjects(factory);
                    gameObjects.add(body);
                } else if (o instanceof StdSpinner) {
                    //TODO : add spinner
                }
            }
        }
    }

    public StdGameProperty getProperty() {
        return property;
    }

    public StdBeatmap getBeatmap() {
        return beatmap;
    }

    public List<DirectStdGameObject> getGameObjects() {
        return gameObjects;
    }
}
