package com.edlplan.framework.graphics.opengl.shader.compile;

import com.edlplan.framework.resource.AResource;
import com.edlplan.framework.utils.StringUtil;

import java.io.IOException;
import java.util.HashMap;

public class CompileRawStringStore {
    public static CompileRawStringStore DEFAULT;

    static {
        DEFAULT = new CompileRawStringStore();
    }

    public static void load(AResource storeDir) {
        try {
            for (String name : storeDir.list("")) {
                if (name.endsWith(".store")) {
                    CompileRawStringStore.get().addToStore(storeDir.loadText(name));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, ProgramNode> map;

    public CompileRawStringStore() {
        map = new HashMap<String, ProgramNode>();
    }

    public String[] getData(String name) {
        return map.get(name).lines;
    }

    public void addToStore(String program) {
        Preprocessor comp = new Preprocessor(program, this);
        comp.compile();
        if (comp.getProgramName() == null) {
            throw new PreCompileException(
                    "you can't add a no name program to store, \n"
                            + "use \"name <@String/name>\" to set name");
        }
        if (map.containsKey(comp.getProgramName())) {
            throw new PreCompileException("program @" + comp.getProgramName() + " already exists");
        }
        map.put(comp.getProgramName(), new ProgramNode(comp.getProgramName(), comp.getResult().split(StringUtil.LINE_BREAK)));
    }


    public static CompileRawStringStore get() {
        return DEFAULT;
    }

    public class ProgramNode {
        public String name;
        public String[] lines;

        public ProgramNode(String name, String[] lines) {
            this.name = name;
            this.lines = lines;
        }
    }
}
