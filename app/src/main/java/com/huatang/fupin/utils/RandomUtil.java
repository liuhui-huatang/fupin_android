package com.huatang.fupin.utils;

import java.util.Random;

/**
 * 功能：随机生成指定位数的验证码
 * <p>
 * 创建： forever
 * 时间： 17/4/19
 */

public class RandomUtil {

    public static String randomNumber(int lenth) {
        String num = "";
        Random random = new Random();

        for (int i = 0; i < lenth; i++) {
            num = num + random.nextInt(9);
        }
        random = null;
        return num;
    }
}