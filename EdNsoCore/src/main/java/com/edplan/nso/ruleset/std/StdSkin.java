package com.edplan.nso.ruleset.std;

import com.edplan.framework.MContext;
import com.edplan.framework.resource.SkinDescription;

public class StdSkin {

    public static final String
            approachcircle = "approachcircle",
            cursor = "cursor",
            cursormiddle = "cursormiddle",
            cursor_smoke = "cursor-smoke",
            cursortrail = "cursortrail",
            followpoint = "followpoint",
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
            slidertrack = "slidertrack";


    public static SkinDescription createSkinDescription(MContext context) {
        SkinDescription description = new SkinDescription(context) {{
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
                            .ref(hitcircleoverlay)
            );
        }};
        return description;
    }


}
