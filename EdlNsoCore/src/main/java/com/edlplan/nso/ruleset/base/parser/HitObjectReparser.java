package com.edlplan.nso.ruleset.base.parser;

import com.edlplan.nso.ruleset.base.game.GameObject;
import com.edlplan.nso.NsoException;

public interface HitObjectReparser<T extends GameObject> {
    public String reparse(T t) throws NsoException;
}
