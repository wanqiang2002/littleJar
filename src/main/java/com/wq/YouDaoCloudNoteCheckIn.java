package com.wq;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;


import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 有道自动获取空间脚本
 */
public class YouDaoCloudNoteCheckIn {

    /**
     * 更改这里的Cookie即可
     */
    private static final String COOKIE = "你的Cookie";

    /**
     * 更改这里的设备参数
     */
    private static final String MY_ARGS = "你的设备参数";

    // =============================================================================
    // ===============================以下内容不要改==================================
    // =============================================================================
    // User-Agent
    private static final String USER_AGENT = "ynote-android";
    // 设备打开
    private static final String DEVICE_OPEN = "http://note.youdao.com/yws/device_open/poll?" + MY_ARGS;
    // 登录奖励
    private static final String LOGIN_REWARD = "https://note.youdao.com/yws/api/daupromotion?method=sync&" + MY_ARGS;
    // 签到
    private static final String CHECK_IN = "https://note.youdao.com/yws/mapi/user?method=checkin&" + MY_ARGS;
    // 看广告
    private static final String AD_PROMPT = "https://note.youdao.com/yws/mapi/user?method=adPrompt&" + MY_ARGS;
    // 看广告随机奖励
    private static final String AD_RANDOM_PROMPT = "https://note.youdao.com/yws/mapi/user?method=adRandomPrompt&" + MY_ARGS;

    public static void main(String[] args) {

        // 每天早上六点执行一次
        CronUtil.schedule("0 0 6 * * ?", (Task) () -> {

            // 打开软件
            deviceOpen();

            // 休息5秒
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 登录获取奖励
            loginReward();

            // 休息5秒
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 签到
            checkIn();

            // ========================================================

            // 休息5秒
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // ========================================================

            // 每隔1分钟执行一次看广告
            while (true) {
                // 看完了广告了!
                if (!adPrompt()) {
                    break;
                }

                try {
                    TimeUnit.SECONDS.sleep(60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // ========================================================

            // 每隔一分钟看广告得一次随机奖励
            while (true) {
                // 看完了广告了!
                if (!adRandomPrompt()) {
                    break;
                }

                try {
                    TimeUnit.SECONDS.sleep(60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("===========================================每天分割线==============================================");
        });

        CronUtil.start();
    }

    /**
     * 开启有道云笔记
     */
    public static void deviceOpen() {
        System.out.println(String.format("[%s] 有道云笔记客户端已启动! 启动信息: %s", DateUtil.now(), getJSON(DEVICE_OPEN)));
    }

    /**
     * 获取一次登录奖励
     */
    public static void loginReward() {
        JSON loginReward = JSONUtil.parse(getJSON(LOGIN_REWARD));
        Long rewardSpace = loginReward.getByPath("rewardSpace", Long.class);
        Long totalRewardSpace = loginReward.getByPath("totalRewardSpace", Long.class);
        Long continuousDays = loginReward.getByPath("continuousDays", Long.class);
        System.out.println(
                String.format(
                        "[%s] 登录奖励获取成功! 获取空间: %s, 连续登录: %s天, 奖励总空间: %s",
                        DateUtil.now(),
                        FileUtil.readableFileSize(rewardSpace),
                        continuousDays,
                        FileUtil.readableFileSize(totalRewardSpace)
                )
        );
    }

    /**
     * 签到方法
     */
    private static void checkIn() {
        JSON checkIn = JSONUtil.parse(getJSON(CHECK_IN));
        Long space = checkIn.getByPath("space", Long.class);
        Long total = checkIn.getByPath("total", Long.class);
        Long success = checkIn.getByPath("success", Long.class);
        Long time = checkIn.getByPath("time", Long.class);
        if (success == 1) {
            System.out.println(
                    String.format(
                            "[%s] [签到] 成功! 获得空间：%s, 获得的总容量：%s, 签到时间：%s",
                            DateUtil.now(),
                            FileUtil.readableFileSize(space),
                            FileUtil.readableFileSize(total),
                            DateUtil.formatDateTime(new Date(time))
                    )
            );
        }
    }

    /**
     * 观看广告
     *
     * @author CY
     */
    private static Boolean adPrompt() {
        JSON watchAd = JSONUtil.parse(getJSON(AD_PROMPT));
        Boolean success = watchAd.getByPath("success", Boolean.class);
        Long todayCount = watchAd.getByPath("todayCount", Long.class);
        Long space = watchAd.getByPath("space", Long.class);
        Long adSpaceTotal = watchAd.getByPath("adSpaceTotal", Long.class);
        if (success) {
            System.out.println(
                    String.format(
                            "[%s] [看广告] 成功! 获得空间：%s, 看广告次数：%s, 广告获取总空间：%s",
                            DateUtil.now(),
                            FileUtil.readableFileSize(space),
                            todayCount,
                            FileUtil.readableFileSize(adSpaceTotal)
                    )
            );
        }
        return success;
    }

    /**
     * 观看广告2
     *
     * @author CY
     */
    private static Boolean adRandomPrompt() {
        JSON watchAd = JSONUtil.parse(getJSON(AD_RANDOM_PROMPT));
        Boolean success = watchAd.getByPath("success", Boolean.class);
        Long firstNotChosenSpace = watchAd.getByPath("firstNotChosenSpace", Long.class);
        Long secondNotChosenSpace = watchAd.getByPath("secondNotChosenSpace", Long.class);
        Long todayCount = watchAd.getByPath("todayCount", Long.class);
        Long space = watchAd.getByPath("space", Long.class);
        Long adSpaceTotal = watchAd.getByPath("adSpaceTotal", Long.class);
        if (success) {
            System.out.println(
                    String.format(
                            "[%s] [看广告随机空间] 成功! 获得空间：%s, 其他两个空间: 左 -> %s, 右 -> %s, 看广告次数：%s, 广告获取总空间：%s",
                            DateUtil.now(),
                            FileUtil.readableFileSize(space),
                            FileUtil.readableFileSize(firstNotChosenSpace),
                            FileUtil.readableFileSize(secondNotChosenSpace),
                            todayCount,
                            FileUtil.readableFileSize(adSpaceTotal)
                    )
            );
        }
        return success;
    }

    /**
     * post请求获取JSON
     *
     * @author CY
     */
    private static String getJSON(String url) {
        Connection connection = Jsoup.connect(url).cookie();
        connection.header("Cookie", COOKIE);
        connection.header("User-Agent", USER_AGENT);
        try {
            return connection.post().text();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}