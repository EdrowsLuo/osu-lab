package com.edplan.nso.ruleset.base.parser;

import com.edplan.nso.ruleset.base.game.GameObject;
import com.edplan.nso.NsoException;

public interface HitObjectReparser<T extends GameObject> {
    public String reparse(T t) throws NsoException;
}
