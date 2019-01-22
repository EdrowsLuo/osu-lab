/**
 *
 * Ruleset {
 *
 *     基础属性 : {};
 *
 *     铺面解析器 -> 铺面 -> 游戏;
 *
 *     游戏 {
 *
 *         Score : 计分
 *
 *         GameObject : 一些物件种类
 *
 *         World : 通过物件构建具体游戏
 *         World : {
 *
 *             Thread1 : 处理判定
 *
 *             Thread2 : 处理绘制
 *
 *         }
 *
 *         WorldBuild : 推荐的游戏构建方式
 *         WorldBuild : {
 *
 *             WorkingGameObject : 当成一个物件的上下文？
 *             WorkingGameObject : {}
 *
 *
 *             Judge : {
 *
 *                 SingleJudge : {
 *                     判定逻辑 : {} //怎么判定
 *                     判定等级 : {} //判定误差怎样
 *                     判定触发 : {} //判定完了之后做啥
 *                 }
 *
 *                 LongJudge : {
 *                     自定义逻辑
 *                     //比如转盘（滑条是多个分别判定的组合）
 *                 }
 *
 *             }
 *
 *
 *             Render : {
 *
 *                 Events : {
 *                     //Judge传递过来的事件，比如超时/发生判定
 *                 }
 *
 *                 Data : {
 *                     //根据特定的数据来更新的对象
 *                 }
 *
 *             }
 *
 *             -------------------------- JudgeThread ------------------------------------------
 *                       [  0  ]
 *             ---------------------------------------------------------------------------------
 *
 *
 *
 *
 *
 *
 *             //对一个物件的最少信息描述
 *
 *             HitObject : {
 *
 *                 HitCircle : {
 *                     Hit : {
 *                         CursorHit : {
 *
 *                             Data : {
 *                                 Time,
 *                                 JudgeLevel
 *                             }
 *
 *                             Action : {
 *                                 TimeOutAction,
 *                                 HitAction
 *                             }
 *
 *                         }
 *                     }
 *
 *                     Render : {
 *
 *                         Combined : { //组合物件
 *
 *                             CirclePiece
 *
 *                             ComboIndex
 *
 *                             Animation : {
 *                                 FadeIn,
 *                                 Expire
 *                             }
 *                         }
 *
 *
 *
 *                     }
 *                 }
 *
 *
 *
 *
 *             }
 *
 *
 *
 *
 *
 *         }
 *
 *     }
 *
 * }
 *
 *
 *
 *
 *
 *
 */





package com.edplan.nso.ruleset.base;