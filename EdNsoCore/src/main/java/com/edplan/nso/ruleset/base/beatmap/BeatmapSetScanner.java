package com.edplan.nso.ruleset.base.beatmap;
import java.io.File;
import com.edplan.nso.NsoCoreBased;
import com.edplan.nso.NsoCore;

/**
 *扫描更新一个单独的BeatmapSet文件夹，并生成json数据缓存
 */
public class BeatmapSetScanner extends NsoCoreBased
{
	private boolean forceLoad=false;
	
	private boolean joinDatabase=true;
	
	public BeatmapSetScanner(NsoCore c){
		super(c);
	}
	
	private void loadFull(File dir){
		
	}
	
	private void loadJson(File jsonf){
		
	}
	
	public void scan(File dir){
		if(forceLoad){
			loadFull(dir);
		}
		File savedJson=new File(dir,".data.json");
		if(savedJson.exists()){
			
		}else{
			loadFull(dir);
		}
	}
}
