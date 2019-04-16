package com.edplan.framework.utils.dataobject.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonCompiler {

    public static final int VERSION_1 = 1;
    public static final int FILE_START_CHECKER = 1933848394;
    public static final int NAME_END_CHECKER = 127361998;
    public static final int STRING_END_CHECKER = 28374889;
    public static final byte MODE_BYTE = 1;
    public static final byte MODE_SHORT = 2;


    HashMap<String, Integer> nameID = new HashMap<>();
    HashMap<String, Integer> stringMap = new HashMap<>();
    int nameMode = 0;
    int stringMode = 0;


    public void compile(JSONObject object, DataOutputStream out) throws IOException {
        addAllNamesAndStrings(object);

        out.writeInt(FILE_START_CHECKER);
        out.writeInt(VERSION_1);
        out.writeInt(nameID.size());
        for (Map.Entry<String, Integer> e : nameID.entrySet()) {
            out.writeInt(e.getValue());
            out.writeUTF(e.getKey());
        }
        out.writeInt(NAME_END_CHECKER);
        out.writeInt(stringMap.size());
        for (Map.Entry<String, Integer> e : stringMap.entrySet()) {
            out.writeInt(e.getValue());
            out.writeUTF(e.getKey());
        }
        out.writeInt(STRING_END_CHECKER);

        if (nameID.size() < Byte.MAX_VALUE) {
            nameMode = MODE_BYTE;
        } else {
            throw new RuntimeException("to many names!");
        }

        if (stringMap.size() < Byte.MAX_VALUE) {
            stringMode = MODE_BYTE;
        } else if (stringMap.size() < Short.MAX_VALUE) {
            stringMode = MODE_SHORT;
        }



    }

    private void addAllNamesAndStrings(JSONObject object) {
        forEach(object, new JsonIterHandler() {

            void addToStrings(Object o) {
                if (o instanceof String) {
                    if (!stringMap.containsKey(o)) {
                        stringMap.put((String) o, stringMap.size());
                    }
                }
            }

            @Override
            public boolean forObjectPair(String name, Object o) {
                if (!nameID.containsKey(name)) {
                    nameID.put(name, nameID.size());
                }
                addToStrings(o);
                return true;
            }

            @Override
            public boolean forArrayItem(int idx, Object o) {
                addToStrings(o);
                return true;
            }
        });
    }


    public static class JsonCompiledTokener extends JSONTokener {
        public JsonCompiledTokener(InputStream in) {
            super(null);
        }
    }

    private static void forEach(Object obj, JsonIterHandler handler) {
        JsonValueOperator operator = new JsonValueOperator() {
            @Override
            public void forNull() {

            }

            @Override
            public void forJSONObject(JSONObject object) {
                try {
                    Iterator<String> keys = object.keys();
                    while (keys.hasNext()) {
                        String n = keys.next();
                        Object o = object.get(n);
                        if (handler.forObjectPair(n, o)) {
                            if (o instanceof JSONObject) {
                                forJSONObject((JSONObject) o);
                            } else if (o instanceof JSONArray) {
                                forJSONArray((JSONArray) o);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void forJSONArray(JSONArray array) {
                final int l = array.length();
                try {
                    for (int i = 0; i < l; i++) {
                        Object o = array.get(i);
                        if (handler.forArrayItem(i, o)) {
                            if (o instanceof JSONObject) {
                                forJSONObject((JSONObject) o);
                            } else if (o instanceof JSONArray) {
                                forJSONArray((JSONArray) o);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void forString(String string) {

            }

            @Override
            public void forNumber(Number number) {

            }
        };
        operator.forJson(obj);
    }

    public interface JsonIterHandler {

        default boolean forObjectPair(String name, Object o) {
            return true;
        }

        default boolean forArrayItem(int idx, Object o) {
            return true;
        }

    }

    public interface JsonValueOperator {

        default void forJson(Object o) {
            if (o == null || o == JSONObject.NULL) {
                forNull();
            } else {
                if (o instanceof JSONObject) {
                    forJSONObject((JSONObject) o);
                } else if (o instanceof JSONArray) {
                    forJSONArray((JSONArray) o);
                } else if (o instanceof String) {
                    forString((String) o);
                } else if (o instanceof Number) {
                    forNumber((Number) o);
                } else {
                    throw new RuntimeException("not json type");
                }
            }
        }

        void forNull();

        void forJSONObject(JSONObject object);

        void forJSONArray(JSONArray array);

        void forString(String string);

        void forNumber(Number number);

    }

}
