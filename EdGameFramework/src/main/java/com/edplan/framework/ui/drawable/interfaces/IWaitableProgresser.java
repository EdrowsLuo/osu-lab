package com.edplan.framework.ui.drawable.interfaces;

/**
 *有头尾两个控制点的progresser,
 *默认当getHeadProgress为0时应该和普通progresser状态相同
 */
public interface IWaitableProgresser extends IProgresser
{
	public void setHeadProgress(float p);
	
	public float getHeadProgress();
}
