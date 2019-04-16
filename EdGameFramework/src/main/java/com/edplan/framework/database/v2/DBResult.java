package com.edplan.framework.database.v2;

import android.support.annotation.NonNull;

import java.util.Iterator;

public interface DBResult extends Iterable<DBLine> {

    default DBLine asOneLine() {
        Iterator<DBLine> iterator = iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    default <T> Iterable<T> as(Class<T> klass) {
        return () -> new Iterator<T>() {

            Iterator<DBLine> iterator = DBResult.this.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next().as(klass);
            }
        };
    }

}
