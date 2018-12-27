package com.edplan.nso.filepart;

import com.edplan.nso.OsuFilePart;
import com.edplan.nso.ruleset.base.game.GameObject;
import com.edplan.nso.ruleset.base.game.GameObjects;
import com.edplan.nso.ruleset.ModeManager;
import com.edplan.nso.NsoException;
import com.edplan.nso.ruleset.std.objects.StdHitObjects;
import com.edplan.nso.ruleset.base.parser.HitObjectReparser;
import com.edplan.nso.ruleset.std.parser.StdHitObjectReparser;

import android.util.Log;

import com.edplan.framework.utils.U;
import com.edplan.nso.ruleset.mania.objects.ManiaHitObjects;
import com.edplan.nso.ruleset.std.objects.StdHitObject;

public class PartHitObjects implements OsuFilePart {
    public static final String TAG = "HitObjects";

    public int mode;

    public GameObjects<?> hitObjects;

    public void initial(int mode) throws NsoException {
        this.mode = mode;
        switch (mode) {
            case ModeManager.MODE_STD:
                hitObjects = new StdHitObjects<StdHitObject>();
                break;
            case ModeManager.MODE_MANIA:
                hitObjects = new ManiaHitObjects();
                break;
            default:
                throw new NsoException("invalid mode : " + mode);
        }
    }

    public void addHitObject(GameObject obj) {
        hitObjects.addHitObject(obj);
    }

    public GameObjects getHitObjects() {
        return hitObjects;
    }

    public <T extends GameObjects> T getHitObjects(Class<T> klass) {
        return (T) getHitObjects();
    }

    public StdHitObjects<StdHitObject> getStdHitObjects() {
        return (StdHitObjects<StdHitObject>) getHitObjects();
    }

    @Override
    public String getTag() {

        return TAG;
    }

    @Override
    public String toString() {

        HitObjectReparser rp;
        switch (mode) {
            case ModeManager.MODE_STD:
                rp = new StdHitObjectReparser();
                break;
            default:
                rp = new StdHitObjectReparser();
                break;
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (GameObject t : hitObjects.getHitObjectList()) {
                sb.append(rp.reparse(t)).append(U.NEXT_LINE);
            }
        } catch (NsoException e) {
            Log.e("reparse", "err build hitObject", e);
        }
        return sb.toString();
    }

}
