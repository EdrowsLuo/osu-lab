package com.edlplan.nso.ruleset.base.beatmap.parser;

import com.edlplan.nso.NsoCore;
import com.edlplan.nso.parser.IniParser;
import com.edlplan.nso.ruleset.base.beatmap.Beatmap;

public interface BeatmapParser<T extends Beatmap> {
    /**
     * 具体的铺面解析
     * @param core NsoCore上下文
     * @param formatVersion 格式版本号，处理向下兼容（？）
     * @param ruleset 使用的Ruleset的id
     * @param generalCache 缓存的General设置
     * @param parserData 储存了Page数据的对象
     * @param info 用来打印错误信息的对象
     * @return 成功的话返回解析出来的Beatmap，否则返回null
     */
    T parse(NsoCore core, int formatVersion,
            String ruleset, IniParser.StdOptionPage generalCache,
            IniParser parserData, BaseDecoder.OpenInfo info);
}
