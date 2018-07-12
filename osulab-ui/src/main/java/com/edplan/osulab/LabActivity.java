package com.edplan.osulab;

import android.os.Environment;
import android.util.Log;
import com.edplan.framework.MContext;
import com.edplan.framework.database.DatabaseTable;
import com.edplan.framework.database.TestDBLine;
import com.edplan.framework.graphics.opengl.MainRenderer;
import com.edplan.framework.main.EdMainActivity;
import com.edplan.framework.main.MainApplication;
import com.edplan.framework.ui.text.font.FontAwesome;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.osulab.ui.BackQuery;
import java.io.File;
import java.io.IOException;
import com.edplan.framework.Framework;

public class LabActivity extends EdMainActivity 
{
	private LabApplication app;

	@Override
	protected void createGame(){
		// TODO: Implement this method
		try {
			File f=new File(Environment.getExternalStorageDirectory(),"osu!lab/logs/log.txt");
			if(f.exists()){
				f.delete();
			}
			File dir=f.getParentFile();
			if(!dir.exists())dir.mkdirs();
			Runtime.getRuntime().exec("logcat -f "+f.getAbsolutePath());
		} catch(IOException e) {
			e.printStackTrace();
			Log.e("log out!","err out put log!",e);
		}
		
		app=new LabApplication();
		app.setUpActivity(this);
		
		//DatabaseTable table=new DatabaseTable();
		//table.initial(TestDBLine.class);
	}
	
	public class LabApplication extends MainApplication
	{
		@Override
		public MainRenderer createRenderer(MContext context){
			// TODO: Implement this method
			return new LabMainRenderer(context,this);
		}

		@Override
		public boolean onBackPressNotHandled(){
			// TODO: Implement this method
			return BackQuery.get().back();
		}

		@Override
		public void onExit(){
			// TODO: Implement this method
			LabGame.get().exit();
		}

		@Override
		public void onGLCreate(){
			// TODO: Implement this method
			double s=Framework.relativePreciseTimeMillion();
			super.onGLCreate();
			try{
				{
					BMFont font=BMFont.loadFont(
						mContext,
						mContext.getAssetResource().subResource("font"),
						"osuFont.fnt");
					font.setErrCharacter(FontAwesome.fa_osu_heart1_break.charvalue);
					BMFont.addFont(font,font.getInfo().face);
				}
				{
					BMFont f=BMFont.getFont(BMFont.FontAwesome);
					f.addFont(
						mContext.getAssetResource().subResource("font"),
						"osuFont.fnt");
				}
			}catch(IOException e){
				e.printStackTrace();
				mContext.toast("读取字体osuFont失败："+e.getMessage());
			}
			System.out.println("load font cost "+(int)(Framework.relativePreciseTimeMillion()-s)+"ms");
		}
	}
}
