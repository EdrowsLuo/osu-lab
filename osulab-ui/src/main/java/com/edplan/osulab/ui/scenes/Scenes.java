package com.edplan.osulab.ui.scenes;
import com.edplan.framework.MContext;
import com.edplan.framework.interfaces.Invoker;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.animation.ComplexAnimation;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.layout.EdMeasureSpec;
import com.edplan.framework.ui.layout.MeasureCore;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.ScenesName;
import com.edplan.osulab.ui.BackQuery;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import com.edplan.osulab.LabGame;
import com.edplan.osulab.ui.scenes.songs.SongsScene;

public class Scenes extends RelativeContainer implements Hideable,BackQuery.BackHandler
{
	public static final String STATIC_GET_SCENE_NAME="getSceneNameStatic";
	
	public static final String STATIC_IS_SINGLE_INSTANCE="isSingleInstanceStatic";
	
	public static final double SCENE_TRANSITION_DURATION=300;
	
	private HashMap<String,SceneNode> scenes=new HashMap<String,SceneNode>();
	
	private static ArrayList<Invoker<Scenes>> registerNodes=new ArrayList<Invoker<Scenes>>();
	
	private ArrayList<BaseScene> scenesStack=new ArrayList<BaseScene>();
	
	private BaseScene currentScene;
	
	public Scenes(MContext c){
		super(c);
		//setAlwaysRefresh(true);
		initialRegister();
	}
	
	public void initialRegister(){
		register(WorkingScene.class,ScenesName.Edit,true);
		register(SongsScene.class);
	}
	
	public void register(Class<? extends BaseScene> klass){
		String name;
		boolean singleInstance;
		try{
			Method getSceneName=klass.getMethod(STATIC_GET_SCENE_NAME,new Class[0]);
			Method isSingleInstance=klass.getMethod(STATIC_IS_SINGLE_INSTANCE,new Class[0]);
			try{
				name=(String)getSceneName.invoke(null,new Object[0]);
				singleInstance=(boolean)isSingleInstance.invoke(null,new Object[0]);
			}catch(Exception e){
				e.printStackTrace();
				return;
			}
		}catch(NoSuchMethodException e){
			e.printStackTrace();
			return;
		}catch(SecurityException e){
			e.printStackTrace();
			return;
		}
		register(klass,name,singleInstance);
	}
	
	public void register(final Class<? extends BaseScene> klass,final String name,final boolean singleInstance){
		if(scenes.containsKey(name)){
			throw new IllegalArgumentException("scene "+name+" register twice");
		}
		SceneNode n=createNode(klass,name,singleInstance);
		scenes.put(n.name,n);
	}

	@Override
	public void onInitialLayouted(){
		// TODO: Implement this method
		super.onInitialLayouted();
		setVisiblility(VISIBILITY_GONE);
		setAlpha(0);
	}

	@Override
	public int getChildrenCount(){
		// TODO: Implement this method
		return currentScene==null?0:1;
	}

	@Override
	public EdView getChildAt(int i){
		// TODO: Implement this method
		return i==0?currentScene:null;
	}
	
	public BaseScene getCurrentScene(){
		return currentScene;
	}
	
	public void swapScene(SceneNode n){
		if(isHidden())show();
		final BaseScene s;
		if(n.singleInstance){
			scenesStack.remove(n.getInstance());
			s=n.getInstance();
		}else{
			s=n.createInstance();
		}

		if(currentScene==null){
			scenesStack.add(s);
			currentScene=s;
			s.show();
		}else{
			currentScene.hide();
			post(new Runnable(){
					@Override
					public void run(){
						// TODO: Implement this method
						scenesStack.remove(currentScene);
						scenesStack.add(s);
						currentScene=s;
						s.show();
					}
				},currentScene.getHideDuration()+20);
		}
	}
	
	public void swapScene(String name){
		final SceneNode n=scenes.get(name);
		if(n==null){
			getContext().toast("无效的场景"+name);
			return;
		}
		swapScene(n);
	}
	
	@Override
	public void hide(){
		// TODO: Implement this method
		BackQuery.get().unregist(this);
		ComplexAnimationBuilder b=ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this,0,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		ComplexAnimation anim=b.buildAndStart();
		anim.setOnFinishListener(setVisibilityWhenFinish(VISIBILITY_GONE));
		setAnimation(anim);
		LabGame.get().getSceneSelectButtonBar().show();
	}

	@Override
	public void show(){
		// TODO: Implement this method
		BackQuery.get().regist(this);
		setVisiblility(VISIBILITY_SHOW);
		ComplexAnimationBuilder b=ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this,1,ViewConfiguration.DEFAULT_TRANSITION_TIME,Easing.None));
		ComplexAnimation anim=b.buildAndStart();
		setAnimation(anim);
	}

	@Override
	public boolean isHidden(){
		// TODO: Implement this method
		return getVisiblility()==VISIBILITY_GONE;
	}

	@Override
	public boolean onBack(){
		// TODO: Implement this method
		if(currentScene.onBackPressed())return true;
		
		if(scenesStack.size()>0){
			currentScene.hide();
			post(new Runnable(){
					@Override
					public void run(){
						// TODO: Implement this method
						scenesStack.remove(currentScene);
						currentScene=scenesStack.size()>0?scenesStack.get(scenesStack.size()-1):null;
						if(currentScene==null){
							hide();
						}else{
							currentScene.show();
						}
					}
				},currentScene.getHideDuration()+20);
			return true;
		}
		return false;
	}
	
	public SceneNode createNode(Class<? extends BaseScene> klass,String name,boolean singleInstance){
		return new SceneNode(klass,name,singleInstance);
	}
	
	public class SceneNode{
		public Class<? extends BaseScene> klass;
		
		public boolean singleInstance;
		
		public String name;
		
		private BaseScene savedScene;
		
		public SceneNode(Class<? extends BaseScene> klass,String name,boolean singleInstance){
			this.klass=klass;
			this.name=name;
			this.singleInstance=singleInstance;
		}
		
		
		public BaseScene createInstance(){
			try{
				BaseScene s=klass.getConstructor(new Class[]{MContext.class}).newInstance(new Object[]{getContext()});
				if(s.getSceneName()==null){
					Method m=klass.getMethod("setSceneName",new Class[]{String.class});
					m.invoke(s,new Object[]{name});
				}
				long wm=
					EdMeasureSpec.makeupMeasureSpec(
					getWidth(),
					EdMeasureSpec.MODE_AT_MOST);
				long hm=
					EdMeasureSpec.makeupMeasureSpec(
					getHeight(),
					EdMeasureSpec.MODE_AT_MOST);
				MeasureCore.measureChild(s,0,0,wm,hm);
				s.layout(0,0,getWidth(),getHeight());
				s.setParent(Scenes.this);
				return s;
			}catch(Exception e){
				e.printStackTrace();
				getContext().toast("err create instance of "+name);
				return null;
			}
		}
		
		public BaseScene getInstance(){
			if(savedScene==null){
				savedScene=createInstance();
			}
			return savedScene;
		}
		
	}
}
