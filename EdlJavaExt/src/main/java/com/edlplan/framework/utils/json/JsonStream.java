package com.edlplan.framework.utils.json;

import com.edlplan.framework.utils.CharArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * parse json, according to a sub set of json grammar
 *
 */
public class JsonStream {

    public static void main(String[] args) throws JSONException {
        {
            JsonStream stream = new JsonStream("{test: 1.23123,\n\"hh\" : [ ]}");
            stream.nextObject();
            System.out.println(stream.nextKey());
            System.out.println(stream.findNextType());
            System.out.println(stream.nextValueString());
            System.out.println(stream.nextKey());
            stream.nextArray();
            stream.endArray();
            stream.endObject();
        }
    }

    private CharArray txt;

    public JsonStream(CharArray txt) {
        this.txt = txt;
    }

    public JsonStream(String s) {
        this(new CharArray(s));
    }

    /**
     * @return if current object or array is closed
     */
    public boolean isClosed() {
        txt.trimBegin();
        return txt.empty() || txt.get(0) == '}' || txt.get(0) == ']';
    }

    public JsonType findNextType() {
        txt.trimBegin();
        if (txt.empty()) return JsonType.Unknown;
        char c = txt.get(0);
        if (c == '.' || (c <= '9' && c >= '0')) {
            return JsonType.Num;
        }
        switch (txt.get(0)) {
            case '[':
                return JsonType.Ary;
            case '{':
                return JsonType.Obj;
            case '"':
                return JsonType.Str;
            default:
                return JsonType.Unknown;
        }
    }

    public String nextString() {
        txt.trimBegin();
        if (txt.empty() || txt.get(0) != '"') {
            throw new CharArray.FormatNotMatchException("not a string!");
        }
        txt.offset++;
        int idx = txt.offset;
        while (idx < txt.end && txt.ary[idx] != '"') {
            idx++;
        }
        if (idx == txt.end) {
            throw new CharArray.FormatNotMatchException("string out of range!");
        }
        String s = new String(txt.ary, txt.offset, idx - txt.offset);
        txt.offset = idx + 1;
        endPart();
        return s;
    }

    public String nextValueString() {
        txt.trimBegin();
        if (txt.empty()) {
            throw new CharArray.FormatNotMatchException("not a string!");
        }
        int idx = txt.offset;
        char tmp;
        boolean hasC = false;
        while (true) {
            if (idx >= txt.end) break;
            tmp = txt.ary[idx];
            if (tmp == '.') {
                if (hasC) {
                    throw new CharArray.FormatNotMatchException("err format double");
                } else {
                    hasC = true;
                    idx++;
                    continue;
                }
            }
            if ((txt.ary[idx] <= '9' && txt.ary[idx] >= '0')) {
                idx++;
            } else {
                break;
            }
        }
        if (idx == txt.end) {
            throw new CharArray.FormatNotMatchException("string out of range!");
        }
        String s = new String(txt.ary, txt.offset, idx - txt.offset);
        txt.offset = idx;
        endPart();
        return s;
    }

    public String nextKey() {
        txt.trimBegin();
        if (txt.empty()) {
            throw new CharArray.FormatNotMatchException("no more txt");
        }
        String key;
        if (txt.get(0) == '"') {
            key = nextKeyString();
        } else {
            key = nextFlatKeyString();
        }
        txt.trimBegin();
        if (txt.empty()) {
            throw new CharArray.FormatNotMatchException("no more txt");
        }
        if (txt.get(0) != ':') {
            throw new CharArray.FormatNotMatchException("no a key value pair");
        }
        txt.offset++;
        return key;
    }

    private String nextKeyString() {
        if (txt.empty() || txt.get(0) != '"') {
            throw new CharArray.FormatNotMatchException("not a string!");
        }
        txt.offset++;
        int idx = txt.offset;
        while (idx < txt.end && txt.ary[idx] != '"') {
            idx++;
        }
        if (idx == txt.end) {
            throw new CharArray.FormatNotMatchException("string out of range!");
        }
        String s = new String(txt.ary, txt.offset, idx - txt.offset);
        txt.offset = idx + 1;
        return s;
    }

    private String nextFlatKeyString() {
        int idx = txt.offset;
        while (idx < txt.end && (txt.ary[idx] != ' ' && txt.ary[idx] != ':')) {
            idx++;
        }
        if (idx == txt.end) {
            throw new CharArray.FormatNotMatchException("string out of range!");
        }
        String s = new String(txt.ary, txt.offset, idx - txt.offset);
        txt.offset = idx;
        return s;
    }

    public void nextObject() {
        txt.trimBegin();
        txt.nextChar('{');
    }

    public void nextArray() {
        txt.trimBegin();
        txt.nextChar('[');
    }

    public void endArray() {
        txt.trimBegin();
        txt.nextChar(']');
        endPart();
    }

    public void endObject() {
        txt.trimBegin();
        txt.nextChar('}');
        endPart();
    }

    private void endPart() {
        txt.trimBegin();
        if ((!txt.empty()) && txt.get(0) == ',') {
            txt.offset++;
        }
    }

    public JSONObject dumpJSONObject() {
        JSONObject object = new JSONObject();
        nextObject();
        try {
            while (!isClosed()) {
                String key = nextKey();
                switch (findNextType()) {
                    case Unknown:
                        throw new CharArray.FormatNotMatchException("err dumping json");
                    case Obj:
                        object.put(key, dumpJSONObject());
                        break;
                    case Ary:
                        object.put(key, dumpJSONArray());
                        break;
                    case Num:
                        object.put(key, nextValueString());
                        break;
                    case Str:
                        object.put(key, nextString());
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CharArray.FormatNotMatchException(e.toString());
        }
        endObject();
        return object;
    }

    public JSONArray dumpJSONArray() {
        JSONArray array = new JSONArray();
        nextArray();
        while (!isClosed()) {
            switch (findNextType()) {
                case Unknown:
                    throw new CharArray.FormatNotMatchException("err dumping json");
                case Obj:
                    array.put(dumpJSONObject());
                    break;
                case Ary:
                    array.put(dumpJSONArray());
                    break;
                case Num:
                    array.put(nextValueString());
                    break;
                case Str:
                    array.put(nextString());
                    break;
            }
        }
        endArray();
        return array;
    }

    public static JSONObject toJSONObject(String txt) {
        return new JsonStream(txt).dumpJSONObject();
    }

    public enum JsonType {
        Obj, Ary, Num, Str, Unknown
    }
}
