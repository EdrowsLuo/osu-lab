package com.edplan.framework.resource;

import com.edplan.framework.MContext;
import com.edplan.framework.async.AsyncTaskContainer;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.GLTexture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SkinDescription {

    private ArrayList<EntryLoader> loaders = new ArrayList<>();

    private MContext context;

    private AsyncTaskContainer container = new AsyncTaskContainer();

    public SkinDescription(MContext context) {
        this.context = context;
        container.setContext(context);
    }

    protected PipeLoader name(String name) {
        return new PipeLoader(name);
    }

    protected String png(String name) {
        return name + ".png";
    }

    public void addLoaders(EntryLoader... l) {
        loaders.addAll(Arrays.asList(l));
    }

    public Skin load(AResource resource) {
        Skin skin = new Skin();
        for (EntryLoader loader : loaders) {
            if (!loader.load(skin, resource, false)) {
                return null;
            }
        }
        return skin;
    }

    /**
     * 读取一个材质列表
     */
    public class RawListLoader implements EntryLoader {

        private String name;

        private String basePath;

        private String suffix;

        private int size;

        public RawListLoader(String name, String basePath,String suffix, int size) {
            this.name=name;
            this.basePath=basePath;
            this.suffix = suffix;
            this.size = size;
        }

        private String indexString(int id) {
            return basePath + "-" + id + suffix;
        }

        @Override
        public boolean load(Skin skin, AResource resource, boolean isOverride) {
            if (resource.contain(indexString(0))) {
                ArrayList<AbstractTexture> textures = new ArrayList<>();
                int idx = 0;
                while (true) {
                    if (size != -1 && idx == size) {
                        break;
                    }
                    String s = indexString(idx);
                    if (resource.contain(s)) {
                        AbstractTexture texture = resource.loadTextureAsync(indexString(idx), container);
                        if (texture == null) {
                            texture = GLTexture.ErrorTexture;
                        }
                        textures.add(texture);
                        idx++;
                    } else {
                        if (size == -1) {
                            break;
                        } else {
                            return false;
                        }
                    }
                }
                skin.putTexture(name, textures);
                return true;
            } else {
                System.out.println("load failed, not found " + name + " : " + basePath);
                return false;
            }
        }
    }

    /**
     * 指向另外一个材质
     */
    public class ReferanceLoader implements EntryLoader {

        private String name;

        private String refName;

        public ReferanceLoader(String name, String refName) {
            this.refName = refName;
            this.name = name;
        }

        @Override
        public boolean load(Skin skin, AResource resource, boolean isOverride) {
            if (skin.contains(refName)) {
                System.out.println("load " + name + " reuse " + name);
                skin.putRawData(name,skin.getTexture(refName));
                return true;
            } else {
                System.out.println("load failed " + name + " reuse " + name);
                return false;
            }
        }
    }

    /**
     * 读取单个材质
     */
    public class RawLoader implements EntryLoader {

        private String name;

        private String path;

        public RawLoader(String name, String path) {
            this.name = name;
            this.path = path;
        }

        @Override
        public boolean load(Skin skin, AResource resource, boolean isOverride) {
            if (resource.contain(path)) {
                System.out.println("load " + name + " : " + path);
                try {
                    GLTexture texture = resource.loadTextureAsync(path, container);
                    skin.putTexture(name, texture);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                System.out.println("load failed " + name + " : " + path);
                return false;
            }
        }
    }

    /**
     * 一个skin加载管道
     */
    public class PipeLoader implements EntryLoader {

        private ArrayList<EntryLoader> pipes = new ArrayList<>();

        private String name;

        public PipeLoader(String name) {
            this.name = name;
        }

        public PipeLoader raw(String path) {
            pipes.add(new RawLoader(name, path));
            return this;
        }

        public PipeLoader rawList(String basePath) {
            return rawList(basePath, ".png", -1);
        }

        public PipeLoader rawList(String basePath, String suffix, int size) {
            pipes.add(new RawListLoader(name, basePath, suffix, size));
            return this;
        }

        public PipeLoader ref(String name) {
            pipes.add(new ReferanceLoader(this.name, name));
            return this;
        }

        @Override
        public boolean load(Skin skin, AResource resource, boolean isOverride) {
            for (EntryLoader loader : pipes) {
                if (loader.load(skin, resource, isOverride)) {
                    return true;
                }
            }
            return isOverride;
        }
    }

    public interface EntryLoader{
        boolean load(Skin skin, AResource resource, boolean isOverride);
    }
}
