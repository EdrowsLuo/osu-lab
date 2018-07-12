package com.edplan.framework.sensor;
import android.hardware.SensorManager;

public class SensorCenter
{
	private static SensorManager manager;
	
	public static boolean hasSensor(SensorType type){
		return false;
	}
	
	
	public enum SensorType{
		Gyroscope
	}
}
