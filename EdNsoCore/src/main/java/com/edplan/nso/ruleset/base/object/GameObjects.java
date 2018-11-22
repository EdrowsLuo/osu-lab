package com.edplan.nso.ruleset.base.object;

import java.util.List;

public abstract class GameObjects<T extends GameObject> {
    public abstract List<T> getHitObjectList();

    public abstract void addHitObject(GameObject t);
}
