package com.edplan.osulab;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.MainRenderer;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.main.MainApplication;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.drawable.RectDrawable;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.AbsoluteLayout;
import com.edplan.framework.ui.widget.FrameContainer;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.TestButton;
import com.edplan.framework.ui.widget.TestScroller;
import com.edplan.framework.ui.widget.ViewPage;
import com.edplan.osulab.ui.opening.MainCircleView;
import com.edplan.framework.ui.widget.RelativeLayout;

public class LabMainRenderer extends MainRenderer
{
	public LabMainRenderer(MContext c,MainApplication app){
		super(c,app);
	}
	
	@Override
	public EdView createContentView(MContext c){
		// TODO: Implement this method
		LabGame.createGame();
		return LabGame.get().createContentView(c);
	}
}
