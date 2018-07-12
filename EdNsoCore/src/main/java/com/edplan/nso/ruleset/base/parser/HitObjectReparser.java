package com.edplan.nso.ruleset.base.parser;
import com.edplan.nso.ruleset.base.object.HitObject;
import com.edplan.nso.NsoException;

public interface HitObjectReparser<T extends HitObject>
{
	public String reparse(T t) throws NsoException;
}
