package com.edlplan.nso.ruleset.base.parser;

import com.edlplan.nso.ruleset.base.game.GameObject;
import com.edlplan.nso.NsoException;

public interface HitObjectParser<T extends GameObject> {
    public T parse(String res) throws NsoException;
}
