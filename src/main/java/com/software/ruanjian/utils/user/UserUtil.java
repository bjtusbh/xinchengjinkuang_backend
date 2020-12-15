package com.software.ruanjian.utils.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.*;

/**
 * @Author LJ
 * @Date 2020/11/4
 * msg
 */

public class UserUtil {
    /**
     *
     * @param strNum 随机字符串数
     * @return  随机字符串
     */
    public static String RandomString(int strNum) {
        return RandomStringUtils.randomAlphanumeric(strNum);
    }

    /**
     * MD5加密
     * @param str   加密字符串--密码
     * @param salt  密钥
     * @return
     */
    public static String encrypt(String str, String salt) {
        return DigestUtils.md5Hex("str={" + str + "}, salt={" + salt + "}");
    }
}
