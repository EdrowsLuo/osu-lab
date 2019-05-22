package com.edlplan.nso;

import java.util.ArrayList;
import java.util.HashMap;

import com.edlplan.nso.ruleset.base.Ruleset;
import com.edlplan.nso.ruleset.base.RulesetNameManager;
import com.edlplan.nso.ruleset.base.beatmap.parser.BeatmapDecoder;
import com.edlplan.framework.async.IntProgressHolder;
import com.edlplan.framework.async.ProgressHolder;
import com.edlplan.framework.MContext;

import java.io.File;
import java.util.List;

import android.os.Environment;

import com.edlplan.framework.utils.FileUtils;
import com.edlplan.nso.ruleset.std.StdRuleset;

/**
 * 将主要游戏的内核部分分离出来。
 * <p>
 * 具体分离出来的部分有：
 * --1：Ruleset的管理
 * --2：Beatmap的解析和地图库的管理，难度的计算
 * --3：具体的游戏画面生成以及对应的接口
 * --4：Storyboard的解析绘制
 */
public class NsoCore {
    private File mainDir;

    private File databaseDir;

    private MContext context;

    private HashMap<String, Ruleset> rulesetsMap = new HashMap<String, Ruleset>();

    private ArrayList<Ruleset> rulesets = new ArrayList<>();

    private RulesetNameManager rulesetNameManager;

    private BeatmapDecoder beatmapDecoder;

    public NsoCore(MContext context) {
        this.context = context;
        this.beatmapDecoder = new BeatmapDecoder(this);
    }

    public BeatmapDecoder getBeatmapDecoder() {
        return beatmapDecoder;
    }

    @SuppressWarnings("unchecked")
    public <T extends Ruleset> T getRulesetById(String id) {
        return (T) rulesetsMap.get(id);
    }

    private void registerRuleset(Ruleset ruleset) {
        rulesetsMap.put(ruleset.getRulesetIdName(), ruleset);
        rulesets.add(ruleset);
    }

    public Loader load() {

        final Loader loader = new Loader();
        loader.loadTread = new Thread(() -> {

            IntProgressHolder mainProgress = new IntProgressHolder();
            mainProgress.setMax(20);
            loader.addProgress(mainProgress);

            /* BEGIN : 加载Ruleset Class */
            mainProgress.setMessage("加载Ruleset Class");
            mainProgress.setIntProgress(1);
            registerRuleset(new StdRuleset(NsoCore.this));

            /* END   : 加载Ruleset Class */

            /* BEGIN : 初始化Ruleset Name */
            mainProgress.setMessage("初始化Ruleset Name");
            mainProgress.setIntProgress(2);

            rulesetNameManager = new RulesetNameManager();
            for (Ruleset r : rulesets) {
                r.applyName(rulesetNameManager);
            }
            /* END   : 初始化Ruleset Name */
            for (Ruleset ruleset : rulesets) {
                ruleset.onLoad();
            }


            /* BEGIN : 加载铺面解析器 */
            mainProgress.setMessage("加载铺面解析器");
            mainProgress.setIntProgress(3);


            /* END   : 加载铺面解析器 */

            if (hasUI()) {
                mainDir = new File(Environment.getExternalStorageDirectory(), "osu!lab");
                FileUtils.checkExistDir(mainDir);

                databaseDir = new File(mainDir, "database");
                FileUtils.checkExistDir(databaseDir);
            }

            if (loader.onLoadComplete != null) {
                loader.onLoadComplete.run();
            }
        });
        return loader;
    }

    protected void initialNames() {

    }

    public RulesetNameManager getRulesetNameManager() {
        return rulesetNameManager;
    }

    public void setContext(MContext context) {
        this.context = context;
    }

    public MContext getContext() {
        return context;
    }

    /**
     * @return if the core is initialled with ui
     */
    public boolean hasUI() {
        return context != null;
    }

    public class Loader {

        protected Thread loadTread;

        private Runnable onLoadComplete;

        ArrayList<ProgressHolder> progressHolders = new ArrayList<>();

        public void start() {
            loadTread.start();
        }

        public synchronized void addProgress(ProgressHolder holder) {
            progressHolders.add(holder);
        }

        public synchronized void removeProgress(ProgressHolder holder) {
            progressHolders.remove(holder);
        }

        public synchronized List<ProgressHolder> getProgresses() {
            return progressHolders;
        }

        public Loader onLoadComplete(Runnable runnable) {
            onLoadComplete = runnable;
            return this;
        }
    }
}

