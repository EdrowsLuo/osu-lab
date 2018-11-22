package com.edplan.nso.ruleset.base.parser;

import com.edplan.nso.ruleset.base.object.GameObject;
import com.edplan.nso.NsoException;

public interface HitObjectParser<T extends GameObject> {
    public T parse(String res) throws NsoException;
}
