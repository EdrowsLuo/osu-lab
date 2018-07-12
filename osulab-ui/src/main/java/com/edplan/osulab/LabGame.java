package com.edplan.osulab;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.osulab.ui.MainBackground;
import com.edplan.osulab.ui.MessageList;
import com.edplan.osulab.ui.OptionList;
import com.edplan.osulab.ui.SceneOverlay;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.osulab.ui.opening.MainCircleView;
import com.edplan.osulab.ui.pieces.BackButton;
import com.edplan.osulab.ui.pieces.JumpingCircle;
import com.edplan.osulab.ui.popup.PopupButtonGroup;
import com.edplan.osulab.ui.popup.RenderStatePopupView;
import com.edplan.osulab.ui.scenes.SceneSelectButtonBar;
import com.edplan.osulab.ui.scenes.Scenes;
import com.edplan.osulab.ui.toolbar.Toolbar;
import com.edplan.nso.NsoCore;

/**
 *全局的管理类
 */
public class LabGame
{
	private static LabGame game;
	
	private Toolbar toolBar;

	private OptionList optionList;
	
	private MessageList messageList;

	private SceneOverlay sceneOverlay;
	
	private JumpingCircle jumpingCircle;
	
	private MainBackground mainBackground;
	
	private SceneSelectButtonBar sceneSelectButtonBar;
	
	private Scenes scenes;
	
	private BackButton backButton;
	
	private MContext context;
	
	private NsoCore nsoCore;

	public void setNsoCore(NsoCore nsoCore){
		this.nsoCore=nsoCore;
	}

	public NsoCore getNsoCore(){
		return nsoCore;
	}

	public void setSceneSelectButtonBar(SceneSelectButtonBar sceneSelectButtonBar){
		this.sceneSelectButtonBar=sceneSelectButtonBar;
	}

	public SceneSelectButtonBar getSceneSelectButtonBar(){
		return sceneSelectButtonBar;
	}

	public void setScenes(Scenes scenes){
		this.scenes=scenes;
	}

	public Scenes getScenes(){
		return scenes;
	}

	public void setMessageList(MessageList messageList){
		this.messageList=messageList;
	}

	public MessageList getMessageList(){
		return messageList;
	}

	public void setBackButton(BackButton backButton){
		this.backButton=backButton;
	}

	public BackButton getBackButton(){
		return backButton;
	}

	public void setMainBackground(MainBackground mainBackground){
		this.mainBackground=mainBackground;
	}

	public MainBackground getMainBackground(){
		return mainBackground;
	}

	public void setJumpingCircle(JumpingCircle jumpingCircle){
		this.jumpingCircle=jumpingCircle;
	}

	public JumpingCircle getJumpingCircle(){
		return jumpingCircle;
	}

	public void setSceneOverlay(SceneOverlay sceneOverlay){
		this.sceneOverlay=sceneOverlay;
	}

	public SceneOverlay getSceneOverlay(){
		return sceneOverlay;
	}

	public void setToolBar(Toolbar toolBar){
		this.toolBar=toolBar;
	}

	public Toolbar getToolBar(){
		return toolBar;
	}

	public void setOptionList(OptionList optionList){
		this.optionList=optionList;
	}

	public OptionList getOptionList(){
		return optionList;
	}
	
	
	public void exit(){
		final PopupButtonGroup p=new PopupButtonGroup(context);
		p.addButton("BACK TO GAME",new EdView.OnClickListener(){
				@Override
				public void onClick(EdView view){
					// TODO: Implement this method
					p.hide();
				}
			});
		p.addButton("EXIT",new EdView.OnClickListener(){
				@Override
				public void onClick(EdView view){
					// TODO: Implement this method
					directExit();
					p.hide();
				}
			});
		p.show();
	}
	
	private void directExit(){
		toolBar.hide();
		optionList.hide();
		sceneOverlay.hide();
		sceneSelectButtonBar.hide();
		jumpingCircle.exitAnimation();
	}
	
	private void initialNsoCore(MContext c){
		nsoCore=new NsoCore(c,null);
	}
	
	public EdView createContentView(MContext c){
		this.context=c;
		
		initialNsoCore(c);
		
		RelativeLayout mainLayout=new RelativeLayout(c);
		RelativeLayout.RelativeParam mp=new RelativeLayout.RelativeParam();
		mp.gravity=Gravity.TopLeft;
		mp.width=Param.makeupScaleOfParentParam(1f);
		mp.height=Param.makeupScaleOfParentParam(1f);
		mainLayout.setLayoutParam(mp);
		
		jumpingCircle=new JumpingCircle(c);
		mainBackground=new MainBackground(c);
		scenes=new Scenes(c);
		backButton=new BackButton(c);
		toolBar=new Toolbar(c);
		{
			RelativeLayout.RelativeParam mparam=new RelativeLayout.RelativeParam();
			mparam.width=Param.MODE_MATCH_PARENT;
			mparam.height=Param.MODE_MATCH_PARENT;
			mainLayout.addView(mainBackground,mparam);
		}
		{
			RelativeLayout.RelativeParam mparam=new RelativeLayout.RelativeParam();
			mparam.width=Param.MODE_MATCH_PARENT;
			mparam.height=Param.MODE_MATCH_PARENT;
			mainLayout.addView(scenes,mparam);
		}
		
		{
			RelativeLayout.RelativeParam param=new RelativeLayout.RelativeParam();
			param.width=Param.MODE_MATCH_PARENT;
			param.height=Param.makeUpDP(UiConfig.TOOLBAR_HEIGHT_DP);
			param.gravity=Gravity.TopCenter;
			mainLayout.addView(toolBar.shadow,param);
		}
		
		{
			sceneSelectButtonBar=new SceneSelectButtonBar(c);
			RelativeLayout.RelativeParam param=new RelativeLayout.RelativeParam();
			param.width=Param.MODE_MATCH_PARENT;
			param.height=Param.makeUpDP(UiConfig.SCENE_SELECT_BUTTON_BAR_HEIGHT);
			param.gravity=Gravity.Center;
			mainLayout.addView(sceneSelectButtonBar,param);
		}
		
		{
			final MainCircleView mainCircleView;
			mainCircleView=new MainCircleView(c);
			RelativeLayout.RelativeParam lllparam=new RelativeLayout.RelativeParam();
			lllparam.width=Param.makeupScaleOfParentOtherParam(0.6f);
			lllparam.height=Param.makeupScaleOfParentParam(0.6f);
			lllparam.gravity=Gravity.Center;
			mainLayout.addView(mainCircleView,lllparam);
		}
		
		{
			RelativeLayout.RelativeParam lllparam=new RelativeLayout.RelativeParam();
			lllparam.width=Param.makeupScaleOfParentOtherParam(0.6f);
			lllparam.height=Param.makeupScaleOfParentParam(0.6f);
			lllparam.gravity=Gravity.Center;
			mainLayout.addView(jumpingCircle,lllparam);
		}
		
		{
			sceneOverlay=new SceneOverlay(c);
			RelativeLayout.RelativeParam param=new RelativeLayout.RelativeParam();
			param.width=Param.MODE_MATCH_PARENT;
			param.height=Param.MODE_MATCH_PARENT;
			param.marginTop=ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP);
			param.gravity=Gravity.BottomCenter;
			mainLayout.addView(sceneOverlay,param);
		}
		
		{
			optionList=new OptionList(c);
			RelativeLayout.RelativeParam param=new RelativeLayout.RelativeParam();
			param.width=Param.makeUpDP(350);
			param.height=Param.MODE_MATCH_PARENT;
			param.marginTop=ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP);
			mainLayout.addView(optionList,param);
		}
		
		{
			messageList=new MessageList(c);
			RelativeLayout.RelativeParam param=new RelativeLayout.RelativeParam();
			param.width=Param.makeUpDP(350);
			param.height=Param.MODE_MATCH_PARENT;
			param.marginTop=ViewConfiguration.dp(UiConfig.TOOLBAR_HEIGHT_DP);
			param.gravity=Gravity.BottomRight;
			mainLayout.addView(messageList,param);
		}
		
		{
			RelativeLayout.RelativeParam param=new RelativeLayout.RelativeParam();
			param.width=Param.MODE_MATCH_PARENT;
			param.height=Param.makeUpDP(UiConfig.TOOLBAR_HEIGHT_DP);
			param.gravity=Gravity.TopCenter;
			mainLayout.addView(toolBar,param);
		}
		
		{
			RelativeLayout.RelativeParam mparam=new RelativeLayout.RelativeParam();
			mparam.width=Param.makeUpDP(40);
			mparam.height=Param.makeUpDP(40);
			mparam.gravity=Gravity.BottomLeft;
			mainLayout.addView(backButton,mparam);
		}
		c.getViewRoot().getRootContainer().setAlpha(0);
		//mainLayout.setVisiblility(EdView.VISIBILITY_GONE);
		//(new RenderStatePopupView(c)).show();
		return mainLayout;
	}
	
	public static void createGame(){
		game=new LabGame();
	}

	public static LabGame get(){
		return game;
	}
}
