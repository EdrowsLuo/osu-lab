package com.edplan.framework.ui.animation;

public interface AnimationCallback
{
	/**
	 *动画开始时调用
	 *注：由于有可能会有用于调整进度的offset，
	 *在此方法中动画的进度不一定是0
	 */
	public void onStart();

	/**
	 *动画过程中被调用
	 *@param p: 当前动画的进度
	 *@param loopCount: 初始值为0，每次进入新循环会+1
	 */
	public void onProgress(double p);

	/**
	 *动画完成时被调用（可能动画被取消，这时不一定会有这个方法的回调，
	 *要保证在动画结束时进行部分操作，请在onEnd()中进行）
	 *注：循环的动画不会进行这个回调
	 */
	public void onFinish();

	/**
	 *总是会在动画结束（包括异常结束）时调用
	 */
	public void onEnd();
}
