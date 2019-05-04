package com.edplan.framework.utils.script.ds;

import com.edplan.framework.utils.Lazy;

import java.util.HashMap;

public class DSDefaultContext implements IDSContext {

    private static Lazy<DSDefaultContext> defaultContextLazy = Lazy.create(DSDefaultContext::new);

    public static DSDefaultContext getDefault() {
        return defaultContextLazy.get();
    }

    private HashMap<String, DSValue> values = new HashMap<>();

    private HashMap<String, DSFunction> functions = new HashMap<>();

    private DSDefaultContext() {
        try {
            addFunction("add.ff", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSFloat) ret).value = ((DSFloat) args[0]).value + ((DSFloat) args[1]).value;

                }

                @Override
                public int argsCount() {
                    return 2;
                }

                @Override
                public char retType() {
                    return 'f';
                }
            });

            addFunction("add.vv", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSVec2) ret).x = ((DSVec2) args[0]).x + ((DSVec2) args[1]).x;
                    ((DSVec2) ret).y = ((DSVec2) args[0]).y + ((DSVec2) args[1]).y;
                }

                @Override
                public int argsCount() {
                    return 2;
                }

                @Override
                public char retType() {
                    return 'v';
                }
            });

            addFunction("sub.ff", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSFloat) ret).value = ((DSFloat) args[0]).value - ((DSFloat) args[1]).value;
                }

                @Override
                public int argsCount() {
                    return 2;
                }

                @Override
                public char retType() {
                    return 'f';
                }
            });

            addFunction("sub.vv", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSVec2) ret).x = ((DSVec2) args[0]).x - ((DSVec2) args[1]).x;
                    ((DSVec2) ret).y = ((DSVec2) args[0]).y - ((DSVec2) args[1]).y;
                }

                @Override
                public int argsCount() {
                    return 2;
                }

                @Override
                public char retType() {
                    return 'v';
                }
            });

            addFunction("minus.f", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSFloat) ret).value = -((DSFloat) args[0]).value;
                }

                @Override
                public int argsCount() {
                    return 1;
                }

                @Override
                public char retType() {
                    return 'f';
                }
            });

            addFunction("minus.v", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSVec2) ret).x = -((DSVec2) args[0]).x;
                    ((DSVec2) ret).y = -((DSVec2) args[0]).y;
                }

                @Override
                public int argsCount() {
                    return 1;
                }

                @Override
                public char retType() {
                    return 'v';
                }
            });

            addFunction("minus.b", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSBoolean) ret).value = !((DSBoolean) args[0]).value;
                }

                @Override
                public int argsCount() {
                    return 1;
                }

                @Override
                public char retType() {
                    return 'b';
                }
            });

            addFunction("_raw.f", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSFloat) ret).value = ((DSFloat) args[0]).value;
                }

                @Override
                public int argsCount() {
                    return 1;
                }

                @Override
                public char retType() {
                    return 'f';
                }
            });

            addFunction("_raw.v", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSVec2) ret).x = ((DSVec2) args[0]).x;
                    ((DSVec2) ret).y = ((DSVec2) args[0]).y;
                }

                @Override
                public int argsCount() {
                    return 1;
                }

                @Override
                public char retType() {
                    return 'v';
                }
            });

            addFunction("_raw.b", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSBoolean) ret).value = ((DSBoolean) args[0]).value;
                }

                @Override
                public int argsCount() {
                    return 1;
                }

                @Override
                public char retType() {
                    return 'v';
                }
            });

            addFunction("vec2.ang.v", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    DSVec2 vec2 = (DSVec2) args[0];
                    ((DSFloat) ret).value = (float) Math.atan2(vec2.y, vec2.x);
                }

                @Override
                public int argsCount() {
                    return 1;
                }

                @Override
                public char retType() {
                    return 'f';
                }
            });

            addFunction("vec2.circle.ff", new DSFunction() {
                @Override
                public void invoke(DSValue ret, DSValue[] args) {
                    ((DSVec2) ret).x = ((DSVec2) args[0]).x + ((DSVec2) args[1]).x;
                    ((DSVec2) ret).y = ((DSVec2) args[0]).y + ((DSVec2) args[1]).y;
                }

                @Override
                public int argsCount() {
                    return 2;
                }

                @Override
                public char retType() {
                    return 'v';
                }
            });

            addValue("Pi", new DSFloat((float) Math.PI));
            addValue("Pi.Half", new DSFloat((float) (Math.PI / 2)));
        } catch (DSException e) {
            e.printStackTrace();
        }
    }

    public void addValue(String name, DSValue value) throws DSException {
        if (getValue(name) != null) {
            throw new DSException("reduplicated value define " + name);
        }
        values.put(name, value);
    }

    public void addFunction(String name, DSFunction function) throws DSException {
        if (getFunction(name) != null) {
            throw new DSException("reduplicated function define " + name);
        }
        functions.put(name, function);
    }

    @Override
    public DSValue getValue(String name) {
        return values.get(name);
    }

    @Override
    public DSFunction getFunction(String name) {
        return functions.get(name);
    }

}
