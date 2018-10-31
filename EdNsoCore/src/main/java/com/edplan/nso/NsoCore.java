package com.edplan.nso;

import java.util.ArrayList;
import java.util.HashMap;

import com.edplan.framework.async.IntProgressHolder;
import com.edplan.framework.async.ProgressHolder;
import com.edplan.nso.ruleset.base.Ruleset;
import com.edplan.nso.ruleset.base.RulesetNameManager;
import com.edplan.nso.ruleset.base.beatmap.BeatmapStorage;
import com.edplan.framework.MContext;

import java.io.File;
import java.util.List;

import android.os.Environment;

import com.edplan.framework.utils.FileUtils;

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

    private NsoConfig config;

    private HashMap<String, Ruleset> rulesetsMap = new HashMap<String, Ruleset>();

    private ArrayList<Ruleset> rulesets = new ArrayList<>();

    private HashMap<Class, Ruleset> class2ruleset = new HashMap<Class, Ruleset>();

    private BeatmapStorage beatmapStorage;

    private RulesetNameManager rulesetNameManager;

    public NsoCore(MContext context, NsoConfig conf) {
        this.config = (conf != null) ? conf : new NsoConfig();
        this.context = context;
        this.beatmapStorage = new BeatmapStorage(this);
    }

    protected Loader load() {

        final Loader loader = new Loader();

        Runnable loadrun = new Runnable() {
            @Override
            public void run() {

                IntProgressHolder mainProgress = new IntProgressHolder();
                mainProgress.setMax(20);
                loader.addProgress(mainProgress);

                /* BEGIN : 加载Ruleset Class */
                mainProgress.setMessage("加载Ruleset Class");
                mainProgress.setIntProgress(1);

                /* END   : 加载Ruleset Class */

                /* BEGIN : 初始化Ruleset Name */
                mainProgress.setMessage("初始化Ruleset Name");
                mainProgress.setIntProgress(2);

                rulesetNameManager = new RulesetNameManager();
                for (Ruleset r : rulesets) {
                    r.applyName(rulesetNameManager);
                }
                /* END   : 初始化Ruleset Name */


                /* BEGIN : 加载铺面解析器 */
                mainProgress.setMessage("加载铺面解析器");
                mainProgress.setIntProgress(3);


                /* END   : 加载铺面解析器 */





                mainDir = new File(Environment.getExternalStorageDirectory(), "osu!lab");
                FileUtils.checkExistDir(mainDir);

                databaseDir = new File(mainDir, "database");
                FileUtils.checkExistDir(databaseDir);
            }
        };
        (new Thread(loadrun)).start();
        return loader;
    }

    protected void initialNames() {

    }

    public RulesetNameManager getRulesetNameManager() {
        return rulesetNameManager;
    }

    protected void setConfig(NsoConfig config) {
        this.config = config;
    }

    public NsoConfig getConfig() {
        return config;
    }

    public void setContext(MContext context) {
        this.context = context;
    }

    public MContext getContext() {
        return context;
    }


    public class Loader {

        ArrayList<ProgressHolder> progressHolders = new ArrayList<>();

        public synchronized void addProgress(ProgressHolder holder) {
            progressHolders.add(holder);
        }

        public synchronized void removeProgress(ProgressHolder holder) {
            progressHolders.remove(holder);
        }

        public synchronized List<ProgressHolder> getProgresses() {
            return progressHolders;
        }

    }
}

