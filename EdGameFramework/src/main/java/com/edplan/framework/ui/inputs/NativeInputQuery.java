package com.edplan.framework.ui.inputs;
import android.view.MotionEvent;
import java.util.List;
import java.util.ArrayList;
import com.edplan.framework.ui.inputs.EdMotionEvent.*;
import com.edplan.framework.MContext;

public class NativeInputQuery
{
	private List<EdMotionEvent> eventQuery1=new ArrayList<EdMotionEvent>(),eventQuery2=new ArrayList<EdMotionEvent>();

	private MContext context;
	
	public NativeInputQuery(MContext c){
		context=c;
	}
	
	public void postEvent(MotionEvent raw){
		synchronized(this){
			eventQuery1.addAll(load(raw));
		}
	}
	
	public List<EdMotionEvent> getQuery(){
		synchronized(this){
			swapQuery();
			List<EdMotionEvent> tmp=new ArrayList<EdMotionEvent>(eventQuery2);
			eventQuery2.clear();
			return tmp;
		}
	}
	
	private void swapQuery(){
		final List<EdMotionEvent> q=eventQuery1;
		eventQuery1=eventQuery2;
		eventQuery2=q;
	}
	
	public List<EdMotionEvent> load(MotionEvent raw){
		if(raw.getPointerId(raw.getActionIndex())>=EdMotionEvent.MAX_POINTER){
			//不对过多点做反应
			return new ArrayList<EdMotionEvent>();
		}
		List<EdMotionEvent> events=new ArrayList<EdMotionEvent>();
		EdMotionEvent.EventType eventType=EdMotionEvent.parseType(raw.getActionMasked());
		if(eventType==EdMotionEvent.EventType.Move||eventType==EdMotionEvent.EventType.Cancel){
			for(int idx=0;idx<raw.getPointerCount();idx++){
				final EdMotionEvent event=new EdMotionEvent();
				final int id=raw.getPointerId(idx);
				event.eventType=eventType;
				event.pointerId=id;
				event.eventPosition.set(raw.getX(idx),raw.getY(idx));
				event.rawType=RawType.TouchScreen;
				event.pointCount=raw.getPointerCount();
				event.time=context.getUiLooper().calTimeOverThread(raw.getEventTime());
				events.add(event);
			}
		}else{
			final EdMotionEvent event=new EdMotionEvent();
			final int idx=raw.getActionIndex();
			final int id=raw.getPointerId(idx);
			event.eventType=eventType;
			event.pointerId=id;
			event.eventPosition.set(raw.getX(idx),raw.getY(idx));
			event.rawType=RawType.TouchScreen;
			event.pointCount=raw.getPointerCount();
			event.time=context.getUiLooper().calTimeOverThread(raw.getEventTime());
			events.add(event);
		}
		
		return events;
	}
}
