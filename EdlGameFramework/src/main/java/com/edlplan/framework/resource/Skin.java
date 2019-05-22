package com.edlplan.framework.resource;

import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Skin {

    private HashMap<String, Object> resMap = new HashMap<>();

    public void putTexture(String name, AbstractTexture texture) {
        resMap.put(name, texture);
    }

    public void putTexture(String name, List<AbstractTexture> textures) {
        resMap.put(name, textures);
    }

    public boolean contains(String name) {
        return resMap.containsKey(name);
    }

    public Object getRawData(String name) {
        return resMap.get(name);
    }

    public void putRawData(String name, Object o) {
        if (o instanceof AbstractTexture || o instanceof List<?>) {
            resMap.put(name, o);
        } else {
            throw new IllegalArgumentException("only texture and texture list is supported");
        }
    }

    public AbstractTexture getTexture(String name) {
        Object r = resMap.get(name);
        if (r == null) {
            return null;
        } else {
            if (r instanceof AbstractTexture) {
                return (AbstractTexture) r;
            } else {
                List<AbstractTexture> list = (List<AbstractTexture>) r;
                return list.isEmpty() ? null : list.get(0);
            }
        }
    }

    public List<AbstractTexture> getTextureList(String name) {
        Object r = resMap.get(name);
        if (r == null) {
            return null;
        } else {
            if (r instanceof AbstractTexture) {
                return Collections.singletonList((AbstractTexture) r);
            } else {
                return (List<AbstractTexture>) r;
            }
        }
    }

}
