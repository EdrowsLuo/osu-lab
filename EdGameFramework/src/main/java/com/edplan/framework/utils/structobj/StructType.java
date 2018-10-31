package com.edplan.framework.utils.structobj;

import com.edplan.framework.utils.DataMap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通过标识key来自动从DataMap获取数据
 */
public class StructType {


    protected boolean onLoad(DataMap dataMap) {
        return false;
    }

    protected final void loadAnnotation(DataMap dataMap) {
        Class klass = this.getClass();

    }

    public final void load(DataMap dataMap) {
        if (onLoad(dataMap)) {
            return;
        }
        loadAnnotation(dataMap);
    }


    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Inject {
        String AUTO_SET = "AUTO_SET";

        String key() default AUTO_SET;

        boolean required() default false;
    }

    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefString {
        String value();
    }

    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefInt {
        int value();
    }

    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefLong {
        long value();
    }

    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefBool {
        boolean value();
    }

    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefDouble {
        double value();
    }

    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefFloat {
        float value();
    }
}
