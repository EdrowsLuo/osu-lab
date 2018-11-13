package com.edplan.nso.ruleset.base.beatmap.parser;

import com.edplan.framework.utils.EmptyConstructor;
import com.edplan.framework.utils.Filter;
import com.edplan.framework.utils.IntFilter;
import com.edplan.nso.ruleset.base.object.StdFormatHitObject;
import com.edplan.superutils.classes.strings.StringSplitter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class StdFormatObjectParser {

    private static StdFormatObjectParser parser = new StdFormatObjectParser();

    public static StdFormatObjectParser get() {
        return parser;
    }

    private List<ObjectCreator<?>> entryList = new LinkedList<>();

    public void register(ObjectCreator<?> creator) {
        if (entryList.contains(creator)) {
            throw new RuntimeException("error: emmm");
        }
        entryList.add(creator);
    }


    public StdFormatHitObject createObjectByType(int type) {
        for (ObjectCreator<?> entry : entryList) {
            final StdFormatHitObject object = entry.create(type);
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    public StdFormatHitObject parse(StringSplitter spl, BaseDecoder.OpenInfo info) {
        try {
            int x = Integer.parseInt(spl.next());
            int y = Integer.parseInt(spl.next());
            int time = Integer.parseInt(spl.next());
            int type = Integer.parseInt(spl.next());
            int hitSound = Integer.parseInt(spl.next());

            StdFormatHitObject object = createObjectByType(type);
            if (object == null) {
                info.error(String.format(Locale.getDefault(),
                        "error parseing %s : not supported type %d", spl.getRes(), type));
                return null;
            }
            object.setX(x);
            object.setY(y);
            object.setTime(time);
            object.setType(type);
            object.setHitSound(hitSound);
            object.parseCustomDatas(spl);
            parseExtra(spl, object, info);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            info.error(String.format("error parseing %s : %s", spl.getRes(), e.getMessage()));
            return null;
        }
    }

    public void parseExtra(StringSplitter spl, StdFormatHitObject hitObject, BaseDecoder.OpenInfo info) {
        try {
            if (spl.hasNext()) {
                hitObject.setRawExtras(spl.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
            info.error(String.format("error parseing extra of %s : %s", spl.getRes(), e.getMessage()));
        }
    }

    @FunctionalInterface
    public interface ObjectCreator<T extends StdFormatHitObject> {
        /**
         * 通过type创建一个物件
         * 当type不支持时返回null
         * @param type
         * @return
         */
        T create(int type);
    }

}
