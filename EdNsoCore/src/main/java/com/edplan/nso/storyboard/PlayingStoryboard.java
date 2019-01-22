package com.edplan.nso.storyboard;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.texture.AutoPackTexturePool;
import com.edplan.framework.graphics.opengl.objs.texture.TexturePool;
import com.edplan.framework.resource.AResource;
import com.edplan.framework.resource.BufferedListResource;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.framework.ui.looper.ExpensiveTask;
import com.edplan.nso.storyboard.elements.IStoryboardElements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayingStoryboard extends EdDrawable {
    private HashMap<String, PlayingStoryboardLayer> layers = new HashMap<String, PlayingStoryboardLayer>();

    private PreciseTimeline timeline;

    private BufferedListResource resource;

    public TexturePool pool;

    public Storyboard storyboard;

    private boolean loaded = false;

    public PlayingStoryboard(MContext context, PreciseTimeline timeline, Storyboard storyboard, AResource resource) {
        super(context);
        this.storyboard = storyboard;
        this.resource = new BufferedListResource(resource);
        this.resource.setIgnoreCase(true);
        this.timeline = timeline;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void load() {
        loadPool();
        for (Map.Entry<String, StoryboardLayer> l : storyboard.getLayers().entrySet()) {
            layers.put(l.getKey(), new PlayingStoryboardLayer(l.getValue(), this));
        }
        loaded = true;
    }

    public void loadUnsync(final Runnable onEnd) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                loadPoolUnsync();
                for (Map.Entry<String, StoryboardLayer> l : storyboard.getLayers().entrySet()) {
                    final Map.Entry<String, StoryboardLayer> fl = l;
                    ExpensiveTask task = new ExpensiveTask(getContext(), new Runnable() {
                        @Override
                        public void run() {

                            layers.put(fl.getKey(), new PlayingStoryboardLayer(fl.getValue(), PlayingStoryboard.this));
                        }
                    });
                    try {
                        task.startAndWait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                onEnd.run();
                loaded = true;
            }
        });
        t.start();
    }

    protected void loadPoolUnsync() {
		/*
		HashMap<String,String> list=new HashMap<String,String>();
		for(Map.Entry<String,StoryboardLayer> l:storyboard.getLayers().entrySet()){
			for(IStoryboardElements ele:l.getValue().getElements()){
				for(String res:ele.getTexturePaths()){
					list.put(res,res);
					
					//if(!list.contains(res)){
					//	list.add(res);
					//}
					
				}
			}
		}
		pool=new AutoPackTexturePool(new TexturePool.TextureLoader(){
				@Override
				public AbstractTexture load(String msg) {

					try {
						//System.out.println("on load texture: "+msg);
						return PlayingStoryboard.this.resource.loadTexture(msg);
					} catch (IOException e) {
						e.printStackTrace();
						//return null;
						throw new RuntimeException(e.getMessage());
					}
				}
			},getContext());
		((AutoPackTexturePool)pool).addAllSynchronized(list.keySet());
		//for(String s:list.keySet()
		((AutoPackTexturePool)pool).setLock(true);
		*/

        TexturePool rawPool = new TexturePool(new TexturePool.TextureLoader() {
            @Override
            public AbstractTexture load(final String msg) {

                try {
                    //System.out.println("on load texture: "+msg);
                    final AbstractTexture[] t = new AbstractTexture[1];
                    ExpensiveTask task = new ExpensiveTask(getContext(), new Runnable() {
                        @Override
                        public void run() {

                            try {
                                t[0] = PlayingStoryboard.this.resource.loadTexture(msg);
                            } catch (IOException e) {
                            }
                        }
                    });
                    task.startAndWait();

                    return t[0];
                } catch (Exception e) {
                    e.printStackTrace();
                    //return null;
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
        for (Map.Entry<String, StoryboardLayer> l : storyboard.getLayers().entrySet()) {
            for (IStoryboardElements ele : l.getValue().getElements()) {
                for (String res : ele.getTexturePaths()) {
                    rawPool.getTexture(res);
                }
            }
        }
        if (GLWrapped.GL_VERSION >= 2) {
            final AutoPackTexturePool[] pooli = new AutoPackTexturePool[1];
            ExpensiveTask task = new ExpensiveTask(getContext(), new Runnable() {
                @Override
                public void run() {

                    pooli[0] = new AutoPackTexturePool(new TexturePool.TextureLoader() {
                        @Override
                        public AbstractTexture load(String msg) {

                            try {
                                //System.out.println("on load texture: "+msg);
                                return PlayingStoryboard.this.resource.loadTexture(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                                //return null;
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                    }, getContext());
                }
            });
            try {
                task.startAndWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pool = pooli[0];
            ((AutoPackTexturePool) pool).addAllSynchronized(rawPool.getAll());
            //if(!Test.get().getBoolean(Test.IS_RELEASE,true))((AutoPackTexturePool)pool).writeToDir(new File("/storage/emulated/0/MyDisk/bin/pool/1"),"pool",null);
        } else {
            pool = rawPool;
        }

    }

    protected void loadPool() {
        TexturePool rawPool = new TexturePool(new TexturePool.TextureLoader() {
            @Override
            public AbstractTexture load(String msg) {

                try {
                    //System.out.println("on load texture: "+msg);
                    return PlayingStoryboard.this.resource.loadTexture(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    //return null;
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
        for (Map.Entry<String, StoryboardLayer> l : storyboard.getLayers().entrySet()) {
            for (IStoryboardElements ele : l.getValue().getElements()) {
                for (String res : ele.getTexturePaths()) {
                    rawPool.getTexture(res);
                }
            }
        }
        if (GLWrapped.GL_VERSION >= 2) {
            pool = new AutoPackTexturePool(new TexturePool.TextureLoader() {
                @Override
                public AbstractTexture load(String msg) {

                    try {
                        //System.out.println("on load texture: "+msg);
                        return PlayingStoryboard.this.resource.loadTexture(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //return null;
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }, getContext());
            pool.addAll(rawPool.getAll());
            //if(!Test.get().getBoolean(Test.IS_RELEASE,true))((AutoPackTexturePool)pool).writeToDir(new File("/storage/emulated/0/MyDisk/bin/pool/1"),"pool",null);
        } else {
            pool = rawPool;
        }

    }

    public PlayingStoryboardLayer getLayer(String key) {
        return layers.get(key);
    }

    public int objectsInField() {
        int c = 0;
        for (PlayingStoryboardLayer l : layers.values()) {
            c += l.objectInField();
        }
        return c;
    }

    public int newApply() {
        int c = 0;
        for (PlayingStoryboardLayer l : layers.values()) {
            c += l.getNewApply();
        }
        return c;
    }

    public TexturePool getTexturePool() {
        return pool;
    }

    public void setTimeline(PreciseTimeline timeline) {
        this.timeline = timeline;
    }

    public PreciseTimeline getTimeline() {
        return timeline;
    }

    @Override
    public void draw(BaseCanvas canvas) {

        if (!loaded) return;
        int i = canvas.save();
        //canvas.getData().getShaders().setColorShader(ColorShader.Invalid);
        //canvas.getData().getShaders().setLegacyTexture3DShader(LegacyTexture3DShader.Invalid);
        for (Map.Entry<String, PlayingStoryboardLayer> e : layers.entrySet()) {
            e.getValue().draw(canvas);
        }
        canvas.restoreToCount(i);
    }
}
