package com.edplan.framework.graphics.opengl.batch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BitmapBatch 
{
	public static Bitmap drawOnBitmap(BaseColorBatch batch,int w,int h){
		Bitmap b=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
		Canvas c=new Canvas(b);
		c.drawColor(0xffffffff);
		Paint linePaint=new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(2);
		linePaint.setARGB(255,0,0,0);
		int ct=batch.getVertexCount()/3;
		for(int i=0;i<ct;i++){
			c.drawLine(
				batch.get(3*i).getPosition().x,batch.get(3*i).getPosition().y,
				batch.get(3*i+1).getPosition().x,batch.get(3*i+1).getPosition().y,
				linePaint);
			c.drawLine(
				batch.get(3*i+1).getPosition().x,batch.get(3*i+1).getPosition().y,
				batch.get(3*i+2).getPosition().x,batch.get(3*i+2).getPosition().y,
				linePaint);
			c.drawLine(
				batch.get(3*i).getPosition().x,batch.get(3*i).getPosition().y,
				batch.get(3*i+2).getPosition().x,batch.get(3*i+2).getPosition().y,
				linePaint);
		}
		return b;
	}
}
