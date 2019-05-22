package com.edlplan.nso.ruleset.base.beatmap;

import com.edlplan.nso.filepart.PartColours;
import com.edlplan.nso.filepart.PartDifficulty;
import com.edlplan.nso.filepart.PartEditor;
import com.edlplan.nso.filepart.PartEvents;
import com.edlplan.nso.filepart.PartGeneral;
import com.edlplan.nso.filepart.PartMetadata;
import com.edlplan.nso.filepart.PartTimingPoints;
import com.edlplan.nso.storyboard.Storyboard;
import com.edlplan.framework.utils.dataobject.DataMapObject;
import com.edlplan.nso.ruleset.base.game.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Beatmap {

    private PartGeneral general;
    private PartEditor editor;
    private PartMetadata metadata;
    private PartDifficulty difficulty;
    private PartEvents event;
    private PartTimingPoints timingPoints;
    private PartColours colours;
    private Storyboard storyboard;

    private String rulesetId;

    private List<GameObject> gameObjects = new ArrayList<>();

    public void addHitObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public List<GameObject> getAllHitObjects() {
        return gameObjects;
    }

    public String getRulesetId() {
        return rulesetId;
    }

    public void setRulesetId(String rulesetId) {
        this.rulesetId = rulesetId;
    }

    public PartGeneral getGeneral() {
        return general;
    }

    public void setGeneral(PartGeneral general) {
        this.general = general;
    }

    public PartEditor getEditor() {
        return editor;
    }

    public void setEditor(PartEditor editor) {
        this.editor = editor;
    }

    public PartMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PartMetadata metadata) {
        this.metadata = metadata;
    }

    public PartDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(PartDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public PartEvents getEvent() {
        return event;
    }

    public void setEvent(PartEvents event) {
        this.event = event;
    }

    public PartTimingPoints getTimingPoints() {
        return timingPoints;
    }

    public void setTimingPoints(PartTimingPoints timingPoints) {
        this.timingPoints = timingPoints;
    }

    public PartColours getColours() {
        return colours;
    }

    public void setColours(PartColours colours) {
        this.colours = colours;
    }

    public Storyboard getStoryboard() {
        return storyboard;
    }

    public void setStoryboard(Storyboard storyboard) {
        this.storyboard = storyboard;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("[%s]", PartGeneral.TAG)).append("\n");
        stringBuilder.append(general.asStructString(DataMapObject.StructOutputType.INI));
        return stringBuilder.toString();
    }
}
