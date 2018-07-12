package com.edplan.framework.utils.stream;

/**
 *一个状态可变的枚举器，相比iterator减少了remove()
 */
public interface IEnum<T>
{
	public boolean hasNext();
	
	public T next();
	
	public T current();
}
