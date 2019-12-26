package org.frank.common.java.string;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @Auto: frank
 * @Date: 2019-12-14
 * @DESC:
 *
 */
public class NumberUtil {
    //获取long型对象值
    public static long getLongValue(Long objLong) {
        return objLong != null ? objLong.longValue() : 0;
    }

    //转换为long
    public static long getLong(String sValue, long dDefalut) {
        try {
            return new Double(sValue).longValue();
        } catch (Exception ex) {
            //ignore
        }
        return dDefalut;
    }

    //获取int型对象值
    public static int getIntValue(Integer objInteger) {
        return objInteger != null ? objInteger.intValue() : 0;
    }

    //转换为int值
    public static int getInt(String sValue, int nDefalut) {
        try {
            return Integer.parseInt(sValue);
        } catch (Exception ex) {
            //ignore
        }
        return nDefalut;
    }

    //转换为double
    public static double getDouble(String sValue, double dDefalut) {
        try {
            return Double.parseDouble(sValue);
        } catch (Exception ex) {
            //ignore
        }
        return dDefalut;
    }

    //把百分数转换为double型的小数,45.34%->0.4534
    public static double convertPercentStringToNum(String sPecent) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        try {
            return nf.parse(sPecent).doubleValue();
        } catch (ParseException e) {
            return 0d;
        }
    }

    //把百分数转换为整型,如45.34%->45
    public static int convertPercentStringToInt(String sPecent) {
        double dPercent = convertPercentStringToNum(sPecent);
        return new Double(dPercent * 100).intValue();
    }
}
