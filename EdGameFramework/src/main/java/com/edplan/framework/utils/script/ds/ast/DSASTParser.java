package com.edplan.framework.utils.script.ds.ast;

import com.edplan.framework.utils.script.ds.ast.expression.DSASTBoolean;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTFloat;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTFunctionCall;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTValue;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTValueArray;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTValueRef;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTVec2;

import java.util.ArrayList;
import java.util.List;

public class DSASTParser {

    public static void main(String[] args) throws DSASTException {
        {
            DSAST dsast = parseExpression("#add((1,1),#add(@pos2, #vec2.circle(@width, #add(@ang, 23.22))))");
            System.out.println(dsast);
        }
        {
            DSAST dsast = parseExpression("23123");
            System.out.println(dsast);
        }
        {
            DSAST dsast = parseExpression("(1.0, 1.0)");
            System.out.println(dsast);
        }
    }

    public static DSAST parseExpression(String s) throws DSASTException {
        return parseExpression(new CharArray(s.toCharArray(), 0, -1));
    }

    public static DSAST parseExpression(CharArray ca) throws DSASTException {
        DSAST dsast = new DSAST();
        List<DSASTEntity> entities = new ArrayList<>();
        entities.add(nextEntity(ca));
        dsast.setEntitys(entities);
        return dsast;
    }

    private static DSASTEntity nextEntity(CharArray s) throws DSASTException {
        s.trimBegin();
        switch (s.get(0)) {
            case '@': {
                return nextRef(s);
            }
            case '#': {
                return nextFunctionCall(s);
            }
            default:
                return nextValue(s);
        }
    }

    private static DSASTValue nextValue(CharArray s) throws DSASTException {
        s.trimBegin();
        if (s.empty()) {
            throw new DSASTException("empty value at " + s.offset);
        }
        switch (s.get(0)) {
            case '(':
                return nextVec2(s);
            case '[':
                return nextArray(s);
            case 't':
            case 'f':
                return nextBoolean(s);
            default:
                return nextFloat(s);
        }
    }

    private static DSASTVec2 nextVec2(CharArray s) throws DSASTException {
        nextLeftBracket(s);
        List<DSASTEntity> args = nextList(s);
        nextRightBracket(s);
        if (args.size() != 2) {
            throw new DSASTException("arg count not match " + s.offset);
        }
        return new DSASTVec2(args.get(0), args.get(1));
    }

    private static DSASTBoolean nextBoolean(CharArray s) throws DSASTException {
        String name = nextName(s);
        if (name.equals("true")) {
            return DSASTBoolean.TRUE;
        } else if (name.equals("false")) {
            return DSASTBoolean.FALSE;
        } else {
            throw new DSASTException(name + " is not a boolean");
        }
    }

    private static DSASTValueArray nextArray(CharArray s) throws DSASTException {
        nextChar(s, '[');
        List<DSASTEntity> values = nextList(s);
        nextChar(s, ']');
        return new DSASTValueArray(values);
    }

    private static DSASTFloat nextFloat(CharArray s) throws DSASTException {
        if (s.empty()) {
            throw new DSASTException("empty float at " + s.offset);
        }
        int findIdx = s.offset;
        char tmp;
        while (findIdx < s.end) {
            tmp = s.ary[findIdx];
            if ((tmp <= '9' && tmp >= '0') || tmp == '.' || tmp == '-' || tmp == '+') {
                findIdx++;
            } else {
                break;
            }
        }
        if (findIdx == s.offset) {
            throw new DSASTException("empty float at " + s.offset);
        }
        String f = new String(s.ary, s.offset, findIdx - s.offset);
        s.offset = findIdx;
        try {
            return new DSASTFloat(Float.parseFloat(f));
        } catch (NumberFormatException e) {
            throw new DSASTException("err format float " + f);
        }
    }

    private static DSASTValueRef nextRef(CharArray s) throws DSASTException {
        nextChar(s, '@');
        return new DSASTValueRef(nextName(s));
    }

    private static DSASTFunctionCall nextFunctionCall(CharArray s) throws DSASTException {
        nextChar(s, '#');
        String name = nextName(s);
        nextLeftBracket(s);
        List<DSASTEntity> args = nextList(s);
        nextRightBracket(s);
        return new DSASTFunctionCall(name, args);
    }

    private static String nextName(CharArray s) throws DSASTException {
        if (s.empty()) {
            throw new DSASTException("empty name at " + s.offset);
        }
        int findIdx = s.offset;
        while (findIdx < s.end & matchName(s.ary[findIdx])) {
            findIdx++;
        }
        if (findIdx == s.offset) {
            throw new DSASTException("empty name at " + s.offset);
        }
        String name = new String(s.ary, s.offset, findIdx - s.offset);
        s.offset = findIdx;
        return name;
    }

    private static boolean matchName(char c) {
        return (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A') || c == '_';
    }

    private static void nextChar(CharArray s, char target) throws DSASTException {
        if (s.empty() || s.get(0) != target) {
            throw new DSASTException(target + " not found at " + s.offset);
        }
        s.offset++;
    }

    private static void nextLeftBracket(CharArray s) throws DSASTException {
        if (s.empty() || s.get(0) != '(') {
            throw new DSASTException("left bracket not found at " + s.offset);
        }
        s.offset++;
    }

    private static void nextRightBracket(CharArray s) throws DSASTException {
        if (s.empty() || s.get(0) != ')') {
            throw new DSASTException("right bracket not found at " + s.offset);
        }
        s.offset++;
    }

    private static List<DSASTEntity> nextList(CharArray s) throws DSASTException {
        List<DSASTEntity> list = new ArrayList<>();
        if (s.empty()) {
            return list;
        }
        while (true) {
            s.trimBegin();
            list.add(nextEntity(s));
            s.trimBegin();
            if ((!s.empty()) && s.get(0) == ',') {
                s.offset++;
            } else {
                break;
            }
        }
        return list;
    }

    public static void nextComma(CharArray s) throws DSASTException {
        s.trimBegin();
        if (s.empty() || s.get(0) != ',') {
            throw new DSASTException("comma not found at " + s.offset);
        }
        s.offset++;
        s.trimBegin();
    }

    public static class DSASTException extends Exception {
        public DSASTException(String m) {
            super(m);
        }
    }

    public static class CharArray {
        public char[] ary;
        public int offset;
        public int end;

        public CharArray(char[] ary, int offset, int end) {
            this.ary = ary;
            this.offset = offset;
            this.end = end == -1 ? ary.length : end;
        }

        public CharArray(CharArray copy) {
            this.ary = copy.ary;
            this.offset = copy.offset;
            this.end = copy.end;
        }

        public char get(int i) {
            return ary[offset + i];
        }

        public void trimBegin() {
            while (offset < end && ary[offset] == ' ') {
                offset++;
            }
        }

        public boolean empty() {
            return offset >= end;
        }

        @Override
        public String toString() {
            return new String(ary, offset, end - offset);
        }
    }

}
