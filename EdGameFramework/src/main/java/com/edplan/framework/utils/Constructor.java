package com.edplan.framework.utils;

public interface Constructor<T>
{
	public T createNewObject(Object... args);
}
