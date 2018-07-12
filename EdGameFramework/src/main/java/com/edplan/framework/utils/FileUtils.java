package com.edplan.framework.utils;

import java.io.File;

public class FileUtils
{
	
	public static void checkExistDir(File dir){
		if(!dir.exists())dir.mkdirs();
	}
	
	
}
