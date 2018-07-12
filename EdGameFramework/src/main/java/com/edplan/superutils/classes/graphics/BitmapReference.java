package com.edplan.superutils.classes.graphics;
import android.graphics.Bitmap;

public class BitmapReference implements AbstractBitmapReference
{
	public Bitmap bitmap;
	
	public BitmapReference(){
		
	}
	
	public BitmapReference(Bitmap res){
		setBitmap(res);
	}

	public void setBitmap(Bitmap bitmap){
		this.bitmap=bitmap;
	}

	@Override
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	
}
