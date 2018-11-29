package com.edplan.nso.ruleset.base.game;

import com.edplan.nso.ruleset.base.game.GameObject;

import java.util.List;

public abstract class GameObjects<T extends GameObject> {
    public abstract List<T> getHitObjectList();

    public abstract void addHitObject(GameObject t);
}
