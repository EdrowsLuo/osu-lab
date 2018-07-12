package com.edplan.framework.ui.looper;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.resource.AResource;
import java.io.IOException;
import com.edplan.framework.interfaces.Wrapper;

public class ExpensiveTask<T>
{
	private Task<T> task;
	
	private MContext context;
	
	private boolean needNotify=false;
	
	private T data;
	
	public Object lock=new Object();
	
	public ExpensiveTask(MContext c,Runnable task){
		this.task=new RunnableWrappedTask<T>(task);
		this.context=c;
	}
	
	public ExpensiveTask(MContext c,Task<T> task){
		this.task=task;
		this.context=c;
	}
	
	public void run(){
		this.task.run(this);
	}

	public void setData(T data){
		this.data=data;
	}

	public T getData(){
		return data;
	}

	public boolean isNeedNotify(){
		return needNotify;
	}
	
	public void startAndWait() throws InterruptedException{
		needNotify=true;
		synchronized(lock){
			post();
			lock.wait();
		}
	}
	
	public void start(){
		needNotify=false;
		post();
	}
	
	protected void post(){
		context.getUiLooper().addExpensiveTask(this);
	}
	
	
	public static AbstractTexture loadTextureSync(MContext context,final AResource res,final String path) throws IOException{
		if(context.checkThread()){
			return res.loadTexture(path);
		}else{
			ExpensiveTask<AbstractTexture> task=new ExpensiveTask<AbstractTexture>(context,new Task<AbstractTexture>(){
					@Override
					public void run(ExpensiveTask<AbstractTexture> task){

						try{
							task.setData(res.loadTexture(path));
						}catch(IOException e){
							e.printStackTrace();
							throw new RuntimeException("err sync load texture",e);
						}
					}
				});
			try{
				task.startAndWait();
			}catch(InterruptedException e){
				
			}
			return task.getData();
		}
	}
	
	public interface Task<T>{
		public void run(ExpensiveTask<T> task);
	}
	
	public class RunnableWrappedTask<T> implements Task<T>
	{
		private final Runnable r;

		public RunnableWrappedTask(Runnable r){
			this.r=r;
		}
		
		@Override
		public void run(ExpensiveTask<T> task){

			r.run();
		}
	}
}
