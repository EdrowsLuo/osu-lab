package com.edplan.framework.utils.script.ds;

import com.edplan.framework.utils.script.ds.ast.DSAST;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTBoolean;
import com.edplan.framework.utils.script.ds.ast.DSASTEntity;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTFloat;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTFunctionCall;
import com.edplan.framework.utils.script.ds.ast.DSASTParser;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTValue;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTValueArray;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTValueRef;
import com.edplan.framework.utils.script.ds.ast.expression.DSASTVec2;

public class DSExpression {

    public static void main(String[] args) throws DSASTParser.DSASTException, DSException {
        if (true) {
            DSOverrideContext context = new DSOverrideContext();
            DSAST dsast = DSASTParser.parseExpression("#add((1,1),#add((1,2),(1,2)))");
            System.out.println(dsast);
            DSExpression expression = new DSExpression(dsast, context);
            System.out.println(expression);
            expression.update();
            System.out.println(expression.getValue());
            context.addValue("test", expression.getValue());
            DSExpression s = new DSExpression(DSASTParser.parseExpression("#add(@test,(1,1))"), context);
            s.update();
            System.out.println(s);
            System.out.println(s.getValue());
        }
        if (true) {
            DSOverrideContext context = new DSOverrideContext();
            DSAST dsast = DSASTParser.parseExpression("#minus(true)");
            System.out.println(dsast);
            DSExpression expression = new DSExpression(dsast, context);
            System.out.println(expression);
            expression.update();
            System.out.println(expression.getValue());
        }
    }

    private DSExpEntry root;

    public DSExpression(DSAST dsast, IDSContext context) throws DSException {
        DSASTEntity entity = dsast.getEntitys().get(0);
        root = toExpEntry(entity, context);
    }

    private DSExpEntry toExpEntry(DSASTEntity entity, IDSContext context) throws DSException {
        if (entity instanceof DSASTValue) {
            if (entity instanceof DSASTFloat) {
                DSExpValueRef valueRef = new DSExpValueRef();
                valueRef.directValue = true;
                valueRef.value = new DSFloat(((DSASTFloat) entity).getValue());
                return valueRef;
            } else if (entity instanceof DSASTVec2) {
                DSASTVec2 vec2 = (DSASTVec2) entity;
                if (vec2.getArg1() instanceof DSASTFloat && vec2.getArg2() instanceof DSASTFloat) {
                    DSExpValueRef valueRef = new DSExpValueRef();
                    valueRef.directValue = true;
                    valueRef.value = new DSVec2(
                            ((DSASTFloat) vec2.getArg1()).getValue(),
                            ((DSASTFloat) vec2.getArg2()).getValue());
                    return valueRef;
                } else {
                    DSExpFuncCall call = new DSExpFuncCall(2);
                    call.putEntry(0, toExpEntry(vec2.getArg1(), context));
                    call.putEntry(1, toExpEntry(vec2.getArg2(), context));
                    call.findFunction("_raw", context);
                    return call;
                }
            } else if (entity instanceof DSASTBoolean) {
                DSExpValueRef valueRef = new DSExpValueRef();
                valueRef.value = new DSBoolean(((DSASTBoolean) entity).getValue());
                return valueRef;
            } else if (entity instanceof DSASTValueArray) {
                DSASTValueArray array = (DSASTValueArray) entity;
                DSExpArray call = new DSExpArray(array.getValues().size());
                final int l = array.getValues().size();
                for (int i = 0; i < l; i++) {
                    call.putEntry(i, toExpEntry(array.getValues().get(i), context));
                }
                call.checkType();
                return call;
            } else {
                throw new DSException("not supported value type " + entity.getClass());
            }
        } else if (entity instanceof DSASTValueRef) {
            DSExpValueRef valueRef = new DSExpValueRef();
            valueRef.value = context.getValue(((DSASTValueRef) entity).getName());
            valueRef.refName = ((DSASTValueRef) entity).getName();
            return valueRef;
        } else {
            DSASTFunctionCall functionCall = (DSASTFunctionCall) entity;
            DSExpFuncCall call = new DSExpFuncCall(functionCall.getArgs().size());
            final int l = functionCall.getArgs().size();
            for (int i = 0; i < l; i++) {
                call.putEntry(i, toExpEntry(functionCall.getArgs().get(i), context));
            }
            call.findFunction(functionCall.getName(), context);
            return call;
        }
    }

    public void update() {
        root.update();
    }

    public DSValue getValue() {
        return root.value;
    }

    @Override
    public String toString() {
        return root.printStruct();
    }

    public static abstract class DSExpEntry {
        DSValue value;

        public void update() {

        }

        public boolean isDirectValue() {
            return false;
        }

        public abstract void printStruct(StringBuilder stringBuilder, int preSteps);

        public static void printStep(StringBuilder stringBuilder,int count) {
            for (int i = 1; i < count; i++) {
                stringBuilder.append("    ");
            }
            if (count > 0) {
                stringBuilder.append("    ");
            }
        }

        public String printStruct() {
            StringBuilder stringBuilder = new StringBuilder();
            printStruct(stringBuilder, 0);
            return stringBuilder.toString();
        }
    }

    public static class DSExpValueRef extends DSExpEntry {

        public String refName;

        protected boolean directValue = false;

        @Override
        public String toString() {
            return "ref: " + super.toString();
        }

        @Override
        public void printStruct(StringBuilder stringBuilder, int preSteps) {
            printStep(stringBuilder, preSteps);
            stringBuilder.append("ref<").append(value.typeName()).append(">(").append(refName).append(")\n");
        }

        @Override
        public boolean isDirectValue() {
            return directValue;
        }
    }

    public static class DSExpArray extends DSExpValueRef {
        DSValue[] callAryCache;
        DSExpEntry[] expEntrys;

        public DSExpArray(int size) {
            callAryCache = new DSValue[size];
            expEntrys = new DSExpEntry[size];
        }

        public void putEntry(int i, DSExpEntry expEntry) {
            expEntrys[i] = expEntry;
            callAryCache[i] = expEntry.value;
        }

        public void checkType() throws DSException {
            String type = null;
            for (DSExpEntry entry : expEntrys) {
                if (type == null) {
                    type = entry.value.typeName();
                } else if (!type.equals(entry.value.typeName())) {
                    throw new DSException("an array can only hold elements with same type");
                }
            }
            value = new DSValueArray(callAryCache);
        }

        @Override
        public void update() {
            final int l = expEntrys.length;
            for (int i = 0; i < l; i++) {
                expEntrys[i].update();
            }
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("array:[");
            for (DSExpEntry entry : expEntrys) {
                stringBuilder.append(entry).append(", ");
            }
            return stringBuilder.toString();
        }
    }

    public static class DSExpFuncCall extends DSExpEntry {
        DSValue[] callAryCache;
        DSExpEntry[] expEntrys;
        DSFunction function;
        String functionName;

        public DSExpFuncCall(int args) {
            callAryCache = new DSValue[args];
            expEntrys = new DSExpEntry[args];
        }

        public void putEntry(int i, DSExpEntry expEntry) {
            expEntrys[i] = expEntry;
            callAryCache[i] = expEntry.value;
        }

        public void findFunction(String name, IDSContext context) {
            functionName = name;
            StringBuilder fixedName = new StringBuilder(name);
            fixedName.append('.');
            for (DSExpEntry entry : expEntrys) {
                fixedName.append(entry.value.typeName());
            }
            function = context.getFunction(fixedName.toString());
            if (function.argsCount() != callAryCache.length) {
                function = null;
            } else {
                if (function.retType() == 'f') {
                    value = new DSFloat();
                } else if (function.retType() == 'v'){
                    value = new DSVec2();
                } else if (function.retType() == 'b') {
                    value = new DSBoolean(false);
                }
            }
        }

        @Override
        public void update() {
            final int l = expEntrys.length;
            for (int i = 0; i < l; i++) {
                expEntrys[i].update();
            }
            function.invoke(value, callAryCache);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("func:[");
            for (DSExpEntry entry : expEntrys) {
                stringBuilder.append(entry).append(", ");
            }
            return stringBuilder.toString();
        }

        @Override
        public void printStruct(StringBuilder stringBuilder, int preSteps) {
            printStep(stringBuilder, preSteps);
            stringBuilder.append("func<").append(value.typeName()).append(">(").append(functionName).append(") : [\n");
            preSteps++;
            for (DSExpEntry entry : expEntrys) {
                entry.printStruct(stringBuilder, preSteps);
            }
            preSteps--;
            printStep(stringBuilder, preSteps);
            stringBuilder.append("]\n");
        }
    }
}
