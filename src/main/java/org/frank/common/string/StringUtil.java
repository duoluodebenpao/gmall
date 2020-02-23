package org.frank.common.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @Auto: frank
 * @Date: 2019-12-14
 * @DESC:
 *
 */
public class StringUtil {
    /**
     * 默认字符编码集
     */
    public static String ENCODING_DEFAULT = "UTF-8";
    public static String GET_ENCODING_DEFAULT = "UTF-8";
    public static String FILE_WRITING_ENCODING = "GBK";
    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 字符串的压缩
     *
     * @param str 待压缩的字符串
     * @return 返回压缩后的字符串
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输出流
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        // 将 b.length 个字节写入此输出流
        gzip.write(str.getBytes());
        gzip.close();
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("ISO-8859-1");
    }

    /**
     * 字符串的解压
     *
     * @param str 对字符串解压
     * @return 返回解压缩后的字符串
     * @throws IOException
     */
    public static String unCompress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes("ISO-8859-1"));
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("utf-8");
    }

    // 加密
    public static String encodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    // 解密
    public static String decodeBase64(String str) {
        byte[] data = Base64.getDecoder().decode(str);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("error in StringUtil", e);
        }
        return null;
    }

    /**
     * 字符串压缩转base64
     *
     * @param str 字符串
     * @return base64
     * @throws IOException
     */
    public static String encodeZipToBase64(String str) throws IOException {
        try {
            return StringUtil.encodeBase64(StringUtil.compress(str));
        } catch (Exception e) {
            logger.error("error in StringUtil", e);
        }
        return null;
    }

    /**
     * 反解base64并解压缩到字符串
     *
     * @param str base64
     * @return 字符串
     * @throws IOException
     */
    public static String decodeBase64ToZip(String str) throws IOException {
        try {
            return StringUtil.unCompress(StringUtil.decodeBase64(str));
        } catch (Exception e) {
            logger.error("error in StringUtil", e);
        }
        return null;
    }

    /**
     * md5字符串
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            logger.error("error in StringUtil", e);
        }
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for (byte x : bs) {
            if ((x & 0xff) >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }

    public static Timestamp checkTimeStamp(Timestamp tt) {
        Timestamp b = Timestamp.valueOf("1970-01-02 00:00:00");
        if (tt.getTime() < b.getTime()) {
            return b;
        }
        return tt;
    }

    /**
     * 字符串自增长
     *
     * @param strings
     * @return
     */
    public static String stringIncr(String strings) {
        int length = strings.length();
        char[] c = new char[length];
        boolean ifzzzz = true;//表示全部为zzzz的字符串
        for (int i = 0; i < length; i++) {
            char tmp = strings.charAt(i);
            if (tmp != "z".charAt(0)) {
                ifzzzz = false;
            }
            c[i] = tmp;
        }
        if (ifzzzz) {
            strings = "a" + strings.replace("z", "a");
            return strings;
        }
        boolean carry = true;//标识是否有进位
        int p = length;
        while (carry) {
            p--;
            char added = StringUtil.letterIncr(c[p]);
            if (added == "a".charAt(0)) {
                c[p] = added;
                carry = true;
            } else {
                c[p] = added;
                carry = false;
            }
        }
        strings = new String(c);
        return strings;
    }

    public static String getShowIdCard(String idCard) {
        String p = idCard.substring(11, 15);
        return idCard.replace(p, "****");
    }

    /**
     * 字母加1
     *
     * @param chars
     * @return
     */
    public static char letterIncr(char chars) {
        if ((chars < 123 && 95 < chars) || (47 < chars && chars < 58)) {
            if (chars == 122) {
                chars = 97;
            } else {
                chars++;
            }
        } else {
        }
        return chars;
    }

    /**
     * 判断指定字符串是否为空
     *
     * @param string 指定的字符串
     * @return 若字符串为空对象（_string==null）或空串（长度为0），则返回true；否则，返回false.
     */
    public static boolean isEmpty(String string) {
        return ((string == null) || (string.trim().length() == 0));
    }

    /**
     * 将0，null undefined 字符串转化为空字符串
     *
     * @param sSrcStr
     * @return
     */
    public static String zero2EmptyStr(String sSrcStr) {
        if (isEmpty(sSrcStr)) {
            return sSrcStr;
        }
        if ("0".equalsIgnoreCase(sSrcStr) || "null".equalsIgnoreCase(sSrcStr) || "undefined".equalsIgnoreCase(sSrcStr)) {
            return "";
        }
        return sSrcStr;
    }

    public static String getRandomString(int length) {
        return getRandomString(length, null);
    }

    public static String getRandomString(int length, String prefix) {
        //定义一个字符串（a-z，0-9）即36位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiop1234567890";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        if (prefix != null) {
            sb.append(prefix);
        }
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 字符串显示处理函数：若为空对象，则返回指定的字符串
     *
     * @see
     */

    public static String showNull(String p_sValue) {
        return showNull(p_sValue, "");
    }

    /**
     * 字符串显示处理函数：若为空对象，则返回指定的字符串
     *
     * @param _sValue         指定的字符串
     * @param _sReplaceIfNull 当_sValue==null时的替换显示字符串；可选参数，缺省值为空字符串（""）
     * @return 处理后的字符串
     */
    public static String showNull(String _sValue, String _sReplaceIfNull) {
        return (_sValue == null ? _sReplaceIfNull : _sValue);
    }

    /**
     * 字符串显示处理函数：若为空对象，则返回指定的字符串
     *
     * @see
     */
    public static String showEmpty(String p_sValue) {
        return showEmpty(p_sValue, "");
    }

    /**
     * 字符串显示处理函数：若为空对象或者空串，则返回指定的字符串
     *
     * @param _sValue          指定的字符串
     * @param _sReplaceIfEmpty 当CMyStirng.isEmpty(_sValue)时的替换显示字符串；可选参数，缺省值为空字符串（""）
     * @return 处理后的字符串
     */
    public static String showEmpty(String _sValue, String _sReplaceIfEmpty) {
        return StringUtil.isEmpty(_sValue) ? _sReplaceIfEmpty : _sValue;
    }

    /**
     * 扩展字符串长度；若长度不足，则是用指定的字符串填充
     *
     * @param string      要扩展的字符串
     * @param length      扩展后的字符串长度。
     * @param chrFill     扩展时，用于填充的字符。
     * @param bFillOnLeft 扩展时，是否为左填充（扩展）；否则，为右填充
     * @return 长度扩展后的字符串
     */
    public static String expandStr(String string, int length, char chrFill, boolean bFillOnLeft) {
        int nLen = string.length();
        if (length <= nLen){
            return string; // 长度已够
        }

        // else,扩展字符串长度
        String sRet = string;
        for (int i = 0; i < length - nLen; i++) {
            sRet = (bFillOnLeft ? chrFill + sRet : sRet + chrFill); // 填充
        }
        return sRet;
    }

    /**
     * 版本号处理函数：拆分企部标版本
     *
     * @param version 版本号
     * @return 处理后的版本列表（部标+企标）
     */
    public static String[] splitVersion(String version) {
        return version.split("\\.", 2);
    }

    /**
     * 设置sStr以endsWith结尾
     *
     * @param sStr
     * @param endsWith
     * @return
     */
    public static String setEndsWith(String sStr, String endsWith) {
        if (StringUtil.isEmpty(sStr)) {
            return endsWith;
        }
        if (sStr.endsWith(endsWith)) {
            return sStr;
        }
        return String.format("%s%s", sStr, endsWith);
    }

    /**
     * 把一个字符串分隔成sql的查询in的形式
     * field1,field2,field3===>'field1','field2','field3'
     *
     * @param sValues
     * @return
     */
    public static String splitStr2SqlInValue(String sValues) {
        if (StringUtil.isEmpty(sValues)) {
            return sValues;
        }
        String[] splitStrs = sValues.split(",");
        return StringUtil.splitStr2SqlInValue(Arrays.asList(splitStrs));
    }

    /**
     * 把列表中的字符串分隔成sql的查询in的形式
     *
     * @param lValueList
     * @return
     */
    public static String splitStr2SqlInValue(List<String> lValueList) {
        if (lValueList == null || lValueList.size() == 0) {
            return "";
        }
        ArrayList aList = new ArrayList(lValueList.size());
        for (String sOneValue : lValueList) {
            aList.add(String.format("'%s'", sOneValue));
        }
        return String.join(",", aList);
    }

    //把数组转换为字符串序列
    public static String numberList2Str(List<Long> lIdList, String sDelimiter) {
        if (lIdList == null || lIdList.size() == 0) {
            return "";
        }
        List<String> strIdList = new ArrayList(lIdList.size());
        lIdList.forEach(record -> strIdList.add(String.valueOf(record)));
        return String.join(sDelimiter, strIdList);
    }

    /**
     * 字符串替换函数：用于将指定字符串中指定的字符串替换为新的字符串。
     *
     * @param strSrc 源字符串。
     * @param strOld 被替换的旧字符串
     * @param strNew 用来替换旧字符串的新字符串
     * @return 替换处理后的字符串
     */
    public static String replaceStr(String strSrc, String strOld, String strNew) {
        if (strSrc == null || strNew == null || strOld == null) {
            return strSrc;
        }

        // 提取源字符串对应的字符数组
        char[] srcBuff = strSrc.toCharArray();
        int nSrcLen = srcBuff.length;
        if (nSrcLen == 0) {
            return "";
        }

        // 提取旧字符串对应的字符数组
        char[] oldStrBuff = strOld.toCharArray();
        int nOldStrLen = oldStrBuff.length;
        if (nOldStrLen == 0 || nOldStrLen > nSrcLen) {
            return strSrc;
        }

        StringBuffer retBuff = new StringBuffer(
                (nSrcLen * (1 + strNew.length() / nOldStrLen)));

        int i, j, nSkipTo;
        boolean bIsFound = false;

        i = 0;
        while (i < nSrcLen) {
            bIsFound = false;

            // 判断是否遇到要找的字符串
            if (srcBuff[i] == oldStrBuff[0]) {
                for (j = 1; j < nOldStrLen; j++) {
                    if (i + j >= nSrcLen) {
                        break;
                    }
                    if (srcBuff[i + j] != oldStrBuff[j]) {
                        break;
                    }
                }
                bIsFound = (j == nOldStrLen);
            }

            // 若找到则替换，否则跳过
            if (bIsFound) { // 找到
                retBuff.append(strNew);
                i += nOldStrLen;
            } else { // 没有找到
                if (i + nOldStrLen >= nSrcLen) {
                    nSkipTo = nSrcLen - 1;
                } else {
                    nSkipTo = i;
                }
                for (; i <= nSkipTo; i++) {
                    retBuff.append(srcBuff[i]);
                }
            }
        }// end while
        srcBuff = null;
        oldStrBuff = null;
        return retBuff.toString();
    }

    /**
     * 获取二位数组中第一列的数据
     *
     * @param strArray
     * @return
     */
    public static List<String> getFirstColumn(String[][] strArray) {
        if (strArray == null || strArray.length == 0) {
            return new ArrayList();
        }

        List<String> firstList = new ArrayList<>(strArray.length);
        for (int i = 0; i < strArray.length; i++) {
            String[] tempArray = strArray[i];
            if (tempArray == null || tempArray.length == 0) {
                continue;
            }
            firstList.add(tempArray[0]);
        }
        return firstList;
    }

    public static String encodeDownloadFileName(String fileName) {
        try {
            return new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.error("encoding error:", e);
            return fileName;
        }
    }

    public static byte[] inputStreamToByteArray(InputStream inputStream) {
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        int i;
        try {
            while ((i = bin.read()) != -1) {
                bao.write(i);
            }
            bao.flush();
            bao.close();
            bin.close();
        } catch (IOException e) {
            logger.error("inputStreamToByteArray err", e);
        }
        return bao.toByteArray();
    }

    /**
     * ip十进制字符串转为字符串    "3227784549" ==> "192.100.21.101"
     *
     * @param ip
     * @return
     */
    public static String transformIp(String ip) {
        int ipInt = (int) Long.parseLong(ip);
        return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
                .append((ipInt >> 16) & 0xff).append('.').append(
                        (ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
                .toString();
    }

    /**
     * ip -> long -> string
     *
     * @param ip
     * @return
     */
    public static String ip2LongString(String ip) {
        try {
            String[] ipArr = ip.split("\\.");
            long ipInt = ((Long.valueOf(ipArr[0]) & 0xFF) << 24) | ((Long.valueOf(ipArr[1]) & 0xFF) << 16) |
                    ((Long.valueOf(ipArr[2]) & 0xFF) << 8) | ((Long.valueOf(ipArr[3]) & 0xFF));
            return String.valueOf(ipInt);
        } catch (Exception e) {
            return "";
        }
    }

    //去除sql中的特殊字符
    public static String filterForSQL(String sContent) {
        if (sContent == null) {
            return "";
        }

        int nLen = sContent.length();
        if (nLen == 0) {
            return "";
        }

        char[] srcBuff = sContent.toCharArray();
        StringBuffer retBuff = new StringBuffer((int) (nLen * 1.5));

        // 各个应用都需要不去除特殊字符，特修改
        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
                case '\'': {
                    retBuff.append("''");
                    break;
                }
                case ';'://为了查询Unicode字符
                    boolean bSkip = false;
                    for (int j = (i + 1); j < nLen && !bSkip; j++) {
                        char cTemp2 = srcBuff[j];
                        if (cTemp2 == ' ') {
                            continue;
                        }
                        if (cTemp2 == '&') {
                            retBuff.append(';');
                        }
                        bSkip = true;
                    }
                    if (!bSkip) {
                        retBuff.append(';');
                    }
                    break;
                default:
                    retBuff.append(cTemp);
            }
        }

        return retBuff.toString();
    }

    /**
     * 字符串截断函数：取指定字符串前部指定长度的字符串； 说明：英文和数字字符长度记1；中文字符长度记2。
     *
     * @return 截断后的字符串。若指定长度小于字符串实际长度，则在返回的字符串后补“...”
     */
    public static String truncateStr(String string, int maxLength) {
        return truncateStr(string, maxLength, "..");
    }

    /**
     * 字符串截断函数：取指定字符串前部指定长度的字符串； 说明：英文和数字字符长度记1；中文字符长度记2。
     *
     * @param sExt 在截断后的字符串上的附加的字符串
     */
    public static String truncateStr(String string, int maxLength,
                                     String sExt, boolean bFullLen) {
        if (string == null) {
            return null;
        }

        if (sExt == null) {
            sExt = "..";
        }

        int nSrcLen = getStringViewWidth(string);
        if (nSrcLen <= maxLength) {
            // 源字符串太短，不需要截断
            return string;
        }

        int nExtLen = getStringViewWidth(sExt);
        if (nExtLen >= maxLength) {
            // 目标长度太短（小于了附加字符串的长度），无法截断。
            return string;
        }

        int iLength = string.length();
        int iRemain = bFullLen ? maxLength : (maxLength - nExtLen);
        StringBuffer sb = new StringBuffer(maxLength + 2); // 附加的“2”是没有意义的，只是为了容错

        for (int i = 0; i < iLength; i++) {
            char aChar = string.charAt(i);
            int iNeed = getCharViewWidth(aChar);
            if (iNeed > iRemain) {
                sb.append(sExt);
                break;
            }
            sb.append(aChar);
            iRemain = iRemain - iNeed;
        }

        return sb.toString();
    }

    /**
     * 字符串截断函数：取指定字符串前部指定长度的字符串； 说明：英文和数字字符长度记1；中文字符长度记2。
     *
     * @param sExt 在截断后的字符串上的附加的字符串
     */
    public static String truncateStr(String string, int maxLength,
                                     String sExt) {
        if (string == null) {
            return null;
        }

        if (sExt == null) {
            sExt = "..";
        }

        int nSrcLen = getStringViewWidth(string);
        if (nSrcLen <= maxLength) {
            // 源字符串太短，不需要截断
            return string;
        }

        int nExtLen = getStringViewWidth(sExt);
        if (nExtLen >= maxLength) {
            // 目标长度太短（小于了附加字符串的长度），无法截断。
            return string;
        }

        int iLength = string.length();
        int iRemain = maxLength - nExtLen;
        StringBuffer sb = new StringBuffer(maxLength + 2); // 附加的“2”是没有意义的，只是为了容错

        for (int i = 0; i < iLength; i++) {
            char aChar = string.charAt(i);
            int iNeed = getCharViewWidth(aChar);
            if (iNeed > iRemain) {
                sb.append(sExt);
                break;
            }
            sb.append(aChar);
            iRemain = iRemain - iNeed;
        }

        return sb.toString();
    }

    //返回指定字符串的显示宽度
    public final static int getStringViewWidth(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int iWidth = 0;
        int iLength = s.length();

        for (int i = 0; i < iLength; i++) {
            iWidth += getCharViewWidth(s.charAt(i));
        }

        return iWidth;
    }

    //返回指定字符的显示宽度，在目前的实现中，认为一个英文字符的显示宽度是1，一个汉字的显示宽度是2。
    public final static int getCharViewWidth(int c) {
        return isChineseChar(c) ? 2 : 1;
    }

    //判断指定的字符是不是汉字，目前是通过判断其值是否大于7FH实现的。
    public final static boolean isChineseChar(int c) {
        return c > 0x7F;
    }

    //=======================
    // 数字转化为字符串

    //将指定整型值转化为字符串
    public static String numberToStr(int nValue) {
        return numberToStr(nValue, 0);
    }

    //将指定整型值转化为字符串
    public static String numberToStr(int nValue, int length) {
        return numberToStr(nValue, length, '0');
    }

    /**
     * 将指定整型值转化为字符串
     *
     * @param nValue  指定整数
     * @param length  转化后字符串长度；若实际长度小于该长度，则使用_chrFill左填充; 可选参数，缺省值0，表示按照实际长度，不扩展。
     * @param chrFill 当整数的实际位数小于指定长度时的填充字符；可选参数，缺省值'0'
     * @return 转化后的字符串
     */
    public static String numberToStr(int nValue, int length, char chrFill) {
        String sValue = String.valueOf(nValue);
        return expandStr(sValue, length, chrFill, true);
    }

    // 将指定长整数转化为字符串,重载：使用long型数值
    public static String numberToStr(long lValue) {
        return numberToStr(lValue, 0);
    }

    //将指定长整数转化为字符串
    public static String numberToStr(long lValue, int length) {
        return numberToStr(lValue, length, '0');
    }

    /**
     * 将指定长整数转化为字符串
     *
     * @param lValue  指定长整数
     * @param length  转化后字符串长度；若实际长度小于该长度，则使用_chrFill左填充; 可选参数，缺省值0，表示按照实际长度，不扩展。
     * @param chrFill 当整数的实际位数小于指定长度时的填充字符；可选参数，缺省值'0'
     * @return 转化后的字符串
     */
    public static String numberToStr(long lValue, int length, char chrFill) {
        String sValue = String.valueOf(lValue);
        return expandStr(sValue, length, chrFill, true);
    }

    /**
     * 判断一个字符串是否在数据中存在,比较时忽略大小写
     *
     * @param sValue
     * @param values
     * @return
     */
    public static boolean valueIgnorecaseInArray(String sValue, String[] values) {
        if (isEmpty(sValue) || values == null || sValue.length() == 0) {
            return false;
        }
        for (String sOneValue : values) {
            if (sValue.equalsIgnoreCase(sOneValue)) {
                return true;
            }
        }
        return false;
    }

    //把一个路径设置为以FileOperator符号结尾
    public static String setEndsWithFileSeparator(String sPath, String sFileOperator) {
        if (StringUtil.isEmpty(sPath)) {
            return sPath;
        }
        if (sPath.endsWith(sFileOperator)) {
            return sPath;
        }

        String sOtherOperator = File.separator.equalsIgnoreCase("//") ? "\\" : "//";
        if (sPath.endsWith(sOtherOperator)) {
            sPath = sPath.substring(0, sPath.length() - 1);
        }
        return String.format("%s%s", sPath, sFileOperator);
    }
}
