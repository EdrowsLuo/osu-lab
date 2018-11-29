package com.edplan.nso.ruleset.base.parser;

import com.edplan.nso.ruleset.base.game.GameObject;
import com.edplan.nso.NsoException;

public interface HitObjectParser<T extends GameObject> {
    public T parse(String res) throws NsoException;
}
