package com.edplan.framework.ui.animation;

public enum AnimState
{
	Running,//动画正常进行
	Waiting,//等待，此时会跳过进度的推进
	Skip,//跳过动画，直接将进度设置为getDuration()
	Stop;//停止动画，保持当前进度
}
