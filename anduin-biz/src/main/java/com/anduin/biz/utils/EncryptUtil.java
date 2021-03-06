package com.anduin.biz.utils;


import com.google.common.math.LongMath;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 简单加密、解密。
 * 算法是将long转成base62/base64间的多次转换。
 */
public class EncryptUtil {

    private static final Log log = LogFactory.getLog(EncryptUtil.class);

    private static final String baseDigits = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = baseDigits.length();
    private static final char[] digitsChar = baseDigits.toCharArray();
    private static final int FAST_SIZE = 'z';
    private static final int[] digitsIndex = new int[FAST_SIZE + 1];

    static {
        for (int i = 0; i < FAST_SIZE; i++) {
            digitsIndex[i] = -1;
        }
        for (int i = 0; i < BASE; i++) {
            digitsIndex[digitsChar[i]] = i;
        }
    }

    /**
     * 转换成10进制
     *
     * @param s 字符串
     * @return 返回的10进制数字
     */
    private static long toBase10(String s) {
        long result = 0L;
        long multiplier = 1;
        for (int pos = s.length() - 1; pos >= 0; pos--) {
            int index = getIndex(s, pos);
            result += index * multiplier;
            multiplier *= BASE;
        }
        return result;
    }

    /**
     * 转换成62进制。
     *
     * @param number 数字
     * @return 返回的62进制字符串
     */
    private static String toBase62(long number) {
        if (number < 0) throw new IllegalArgumentException("Number must be positive: " + number);
        if (number == 0) return "0";
        StringBuilder buf = new StringBuilder();
        while (number != 0) {
            buf.append(digitsChar[(int) (number % BASE)]);
            number /= BASE;
        }
        return buf.reverse().toString();
    }

    private static int getIndex(String s, int pos) {
        char c = s.charAt(pos);
        if (c > FAST_SIZE) {
            throw new IllegalArgumentException("encode or decode error");
        }
        int index = digitsIndex[c];
        if (index == -1) {
            throw new IllegalArgumentException("encode or decode error");
        }
        return index;
    }

    /**
     * 获取最后一位
     *
     * @param number 数字
     * @return 返回的最后一位字母
     */
    private static String lastLetter(long number) {
        return String.valueOf(digitsChar[LongMath.mod(number, BASE)]);
    }

    /**
     * 获取第一位
     *
     * @param number 数字
     * @return 返回的第一个字母
     */
    private static String firstLetter(long number) {
        String letter = "";
        try {
            String n = StringUtils.reverse(Long.toString(number));
            number = NumberUtils.toLong(n);
            letter = String.valueOf(digitsChar[LongMath.mod(number, BASE)]);
        } catch (Exception e) {
            log.error("数据转换异常", e);
        }
        return letter;
    }

    /**
     * 将number进行加密
     *
     * @param number 要加密的数字
     * @return 加密过后的字符串
     */
    public static String encode(long number) {
        String base62 = "";
        try {
            String n = Long.toString(number);
            n = "1" + StringUtils.reverse(n);
            Long number2 = NumberUtils.toLong(n) * 7;
            base62 = firstLetter(number) + StringUtils.reverse(toBase62(number2)) + lastLetter(number);
        } catch (Exception e) {
            log.error("数据加密异常", e);
        }
        return base62;
    }

    /**
     * 加密字符串解密成long,如果转换失败则返回0
     *
     * @param  encodeStr 加密字符串
     * @return 解密过的数字
     */
    public static long decode(String encodeStr) {
        try {
            String str = encodeStr.substring(1, encodeStr.length() - 1);
            long number = toBase10(StringUtils.reverse(str));
            number = number / 7;
            String n = Long.toString(number);
            n = StringUtils.reverse(n);
            n = n.substring(0, n.length() - 1);
            number = NumberUtils.toLong(n);

            String firstLetter = firstLetter(number);
            String lastLetter = lastLetter(number);
            if (encodeStr.startsWith(firstLetter) && encodeStr.endsWith(lastLetter)) {
                return number;
            }
            return 0;
        } catch (Exception e) {
            log.error("解密异常", e);
        }
        return 0;
    }


}
