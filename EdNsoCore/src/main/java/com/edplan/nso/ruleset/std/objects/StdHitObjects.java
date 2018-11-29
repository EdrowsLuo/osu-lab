package com.edplan.nso.ruleset.std.objects;

import com.edplan.nso.ruleset.base.game.GameObject;
import com.edplan.nso.ruleset.base.game.GameObjects;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class StdHitObjects<T extends StdHitObject> extends GameObjects<T> {
    private List<T> hitObjects;

    public StdHitObjects() {
        hitObjects = new ArrayList<T>();
    }

    public int size() {
        return hitObjects.size();
    }

    public StdHitObject get(int i) {
        return hitObjects.get(i);
    }

    @Override
    public void addHitObject(GameObject t) {

        if (t instanceof StdHitObject) {
            hitObjects.add((T) t);
        }
    }

    @Override
    public List<T> getHitObjectList() {

        return hitObjects;
    }
}
