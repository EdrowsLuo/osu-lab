package com.edplan.framework.async;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.looper.ExpensiveTask;

public class AsyncTaskContainer
{
	private MContext context;
	
	private Thread holdingThread;
	
	private AsyncTask task;
	
	private String stepMessage="";
	
	private int stepCode=-1;
	
	private int stepCount=1;
	
	public AsyncTaskContainer(){
		
	}
	
	public float getProgress(){
		return getStepCode()/(float)getStepCount();
	}

	public void setStepCount(int stepCount){
		this.stepCount=stepCount;
	}
	
	public void nextStep(){
		stepCode++;
	}
	
	public void nextStep(String msg){
		stepCode++;
	}

	public int getStepCount(){
		return stepCount;
	}

	public int getStepCode(){
		return stepCode;
	}

	public String getStepMessage(){
		return stepMessage;
	}

	public AsyncTaskContainer setContext(MContext context){
		this.context=context;
		return this;
	}

	public MContext getContext(){
		return context;
	}
	
	public boolean hasContext(){
		return getContext()!=null;
	}
	
	public final void run(){
		try{
			if(task!=null){
				task.run(this);
			}
			stepCode=stepCount;
			onFinish();
			onEnd(true);
		}catch(Exception e){
			e.printStackTrace();
			onErr(e);
			onEnd(false);
		}
	}
	
	protected void onFinish(){
		
	}
	
	protected void onErr(Exception e){
		
	}
	
	protected void onEnd(boolean state){
		
	}
	
	public void doSomeingInUiThread(Runnable r) throws InterruptedException{
		if(hasContext()){
			ExpensiveTask task=new ExpensiveTask(getContext(),r);
			task.startAndWait();
		}else{
			throw new IllegalStateException("you hadn't set a context to this task");
		}
	}
	
	public final void start(){
		if(holdingThread==null){
			holdingThread=new Thread(new Runnable(){
					@Override
					public void run(){

						AsyncTaskContainer.this.run();
					}
				});
			holdingThread.start();
		}
	}
}
