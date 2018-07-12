package com.edplan.nso.ruleset.base.parser;
import com.edplan.nso.ruleset.base.object.HitObject;
import com.edplan.nso.NsoException;

public interface HitObjectParser<T extends HitObject>
{
	public T parse(String res) throws NsoException;
}
