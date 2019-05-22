package com.edlplan.nso.ruleset.std;

import com.edlplan.framework.MContext;
import com.edlplan.framework.resource.SkinDescription;

public class StdSkin {

    public static final String
            approachcircle = "approachcircle",
            cursor = "cursor",
            cursormiddle = "cursormiddle",
            cursor_smoke = "cursor-smoke",
            cursortrail = "cursortrail",
            followpoint = "followpoint-",
            hit0_0 = "hit0-0",
            hit50_0 = "hit50-0",
            hit100_0 = "hit100-0",
            hit100k_0 = "hit100k-0",
            hit300_0 = "hit300-0",
            hit300g_0 = "hit300g-0",
            hit300k_0 = "hit300k-0",
            hitcircle = "hitcircle",
            hitcircleoverlay = "hitcircleoverlay",
            sliderendcircle = "sliderendcircle",
            sliderendcircleoverlay = "sliderendcircleoverlay",
            sliderstartcircle = "sliderstartcircle",
            sliderstartcircleoverlay = "sliderstartcircleoverlay",
            sliderb = "sliderb",
            sliderfollowcircle = "sliderfollowcircle",
            sliderscorepoint = "sliderscorepoint",
            slidertrack = "slidertrack",
            reversearrow = "reversearrow";


    public static SkinDescription createSkinDescription(MContext context) {
        return new SkinDescription(context) {{
            addLoaders(
                    name(approachcircle)
                            .raw(png(approachcircle)),
                    name(cursor)
                            .raw(png(cursor)),
                    name(cursormiddle)
                            .raw(png(cursormiddle)),
                    name(cursor_smoke)
                            .raw(png(cursor_smoke)),
                    name(cursortrail)
                            .raw(png(cursortrail)),
                    name(followpoint)
                            .rawList(followpoint),
                    name(hit0_0)
                            .raw(png(hit0_0)),
                    name(hit50_0)
                            .raw(png(hit50_0)),
                    name(hit100_0)
                            .raw(png(hit100_0)),
                    name(hit300_0)
                            .raw(png(hit300_0)),
                    name(hit100k_0)
                            .raw(png(hit100k_0)),
                    name(hit300g_0)
                            .raw(png(hit300g_0)),
                    name(hit100k_0)
                            .raw(png(hit300k_0)),
                    name(hitcircle)
                            .raw(png(hitcircle)),
                    name(hitcircleoverlay)
                            .raw(png(hitcircleoverlay)),
                    name(sliderb)
                            .rawList(sliderb),
                    name(sliderscorepoint)
                            .raw(png(sliderscorepoint)),
                    name(sliderendcircle)
                            .raw(png(sliderendcircle))
                            .ref(hitcircle),
                    name(sliderendcircleoverlay)
                            .raw(png(sliderendcircleoverlay))
                            .ref(hitcircleoverlay),
                    name(sliderstartcircle)
                            .raw(png(sliderstartcircle))
                            .ref(hitcircle),
                    name(sliderstartcircleoverlay)
                            .raw(png(sliderstartcircleoverlay))
                            .ref(hitcircleoverlay),
                    name(reversearrow)
                            .raw(png(reversearrow))
            );
        }};
    }


}
