package com.edplan.nso.parser;
import com.edplan.nso.NsoException;

public interface LinesParser
{
	public boolean parse(String l) throws NsoException;
}
