package org.frank.common.date;

import org.frank.common.string.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wuguowei on 2018/7/21.
 */
public class DateTimeHelper {

    /**
     * 对一个时间日期类型增加固定的小时数,时间格式为: yyyy-MM-dd HH:mm:ss
     *
     * @param _sDateTime
     * @param _nAddHours 大于0,则往后推几小时;小于0,则往前推几小时
     * @return
     * @throws Exception
     */
    public static String dateAddHour(String _sDateTime, int _nAddHours) throws Exception {
        return dateAddFieldVlalues(_sDateTime, MyDateTime.DEF_DATETIME_FORMAT_PRG, MyDateTime.HOUR, _nAddHours);
    }

    /**
     * 对一个时间日期类型增加固定的天数,时间格式为: yyyy-MM-dd HH:mm:ss
     *
     * @param _sDateTime
     * @param _nAddDays  大于0,则往后推几天;小于0,则往前推几天
     * @return
     * @throws Exception
     */
    public static String dateAddDay(String _sDateTime, int _nAddDays) throws Exception {
        return dateAddFieldVlalues(_sDateTime, MyDateTime.DEF_DATETIME_FORMAT_PRG, MyDateTime.DAY, _nAddDays);
    }

    public static String dateAddFieldVlalues(String _sDateTime, String _sDateTimeFormat, int _nAddField, int _nAddValue) throws Exception {
        MyDateTime dateTime = new MyDateTime();
        try {
            dateTime.setDateTimeWithString(_sDateTime, _sDateTimeFormat);
            dateTime.dateAdd(_nAddField, _nAddValue);
            return dateTime.toString(_sDateTimeFormat);
        } catch (Exception ex) {
            throw new Exception("format datetime[" + _sDateTime + "], addDays=" + _nAddValue + " error", ex);
        }
    }

    // 分钟转换格式化
    public static String transformMinute(int minutes) {
        int hours = (int) Math.floor(minutes / 60);
        if (hours >= 1) {
            minutes = minutes % 60;
        }
        int days = (int) Math.floor(hours / 24);
        if (days >= 1) {
            hours = hours % 24;
            return String.format("%d天%d小时%d分钟", days, hours, minutes);
        }
        if (hours >= 1) {
            return String.format("%d小时%d分钟", hours, minutes);
        }
        return String.format("%d分钟", minutes);
    }

    // get default begin time
    public static String getDefaultBeginTime(String beginTime, String endTime) throws Exception {
        if (StringUtil.isEmpty(beginTime)) {
            beginTime = DateTimeHelper.dateAddDay(endTime, -30);
        }
        return beginTime;
    }

    // get default end time
    public static String getDefaultEndTime(String endTime) throws Exception {
        if (StringUtil.isEmpty(endTime)) {
            try {
                MyDateTime myDateTime = MyDateTime.now();
                endTime = String.format("%s 00:00:00", myDateTime.toString(MyDateTime.DEF_DATE_FORMAT_PRG));
            } catch (Exception ex) {
                throw new Exception("init end time error");
            }
        }
        return endTime;
    }

    /**
     * 获取今天开始时间
     *
     * @return
     * @throws Exception
     */
    public static String getTodayBegin() throws Exception {
        String beginTime = "";
        try {
            MyDateTime myDateTime = MyDateTime.now();
            beginTime = String.format("%s 00:00:00", myDateTime.toString(MyDateTime.DEF_DATE_FORMAT_PRG));
        } catch (Exception ex) {
            throw new Exception("getTodayBegin time error");
        }
        return beginTime;
    }

    /**
     * 获取今天结束时间
     *
     * @return
     * @throws Exception
     */
    public static String getTodayEnd() throws Exception {
        String endTime = "";
        try {
            MyDateTime myDateTime = MyDateTime.now();
            endTime = String.format("%s 23:59:59", myDateTime.toString(MyDateTime.DEF_DATE_FORMAT_PRG));
        } catch (Exception ex) {
            throw new Exception("getTodayEnd time error");
        }
        return endTime;
    }

    public static List<String> getDateList(String beginTime, String endTime) throws Exception {
        List<String> dateList = new ArrayList<>();
        MyDateTime begin = new MyDateTime();
        begin.setDateTimeWithString(beginTime, MyDateTime.DEF_DATETIME_FORMAT_PRG);
        MyDateTime end = new MyDateTime();
        end.setDateTimeWithString(endTime, MyDateTime.DEF_DATETIME_FORMAT_PRG);
        while (begin.getDateTime().before(end.getDateTime())) {
            dateList.add(begin.toString(MyDateTime.DEF_DATE_FORMAT_PRG));
            begin = begin.dateAdd(MyDateTime.DAY, 1);
        }
        return dateList;
    }

    //计算开始时间到结束时间,以小时为单位的连续时间戳
    public static HashMap<String, String> makeStatTimeHourContinuous(String _sBeginTime, String _sEndTime) throws Exception {
        if (StringUtil.isEmpty(_sBeginTime) || StringUtil.isEmpty(_sEndTime)) {
            return new HashMap<>();
        }
        return makeStatTimeContinuous(MyDateTime.HOUR, _sBeginTime, _sEndTime);
    }

    //计算开始时间到结束时间,以天为单位的连续时间戳
    public static HashMap<String, String> makeStatTimeDayContinuous(String _sBeginTime, String _sEndTime) throws Exception {
        if (StringUtil.isEmpty(_sBeginTime) || StringUtil.isEmpty(_sEndTime)) {
            return new HashMap<>();
        }

        return makeStatTimeContinuous(MyDateTime.DAY, _sBeginTime, _sEndTime);
    }

    public static HashMap<String, String> makeStatTimeContinuous(int _nMydateTimeField, String _sBeginTime, String _sEndTime) throws Exception {
        MyDateTime beginDateTime = new MyDateTime();
        beginDateTime.setDateTimeWithString(_sBeginTime, MyDateTime.DEF_DATETIME_FORMAT_PRG);
        MyDateTime endDateTime = new MyDateTime();
        endDateTime.setDateTimeWithString(_sEndTime, MyDateTime.DEF_DATETIME_FORMAT_PRG);

        HashMap<String, String> continueStatTimeMap = new HashMap<>();
        int nSafeFactor = 0;//最多计算1000次,防止死循环
        while (nSafeFactor < 1000 && beginDateTime.getTimeInMillis() <= endDateTime.getTimeInMillis()) {
            continueStatTimeMap.put(beginDateTime.toString(MyDateTime.DEF_DATETIME_FORMAT_PRG), "1");
            beginDateTime.dateAdd(_nMydateTimeField, 1);
            nSafeFactor++;
        }

        return continueStatTimeMap;
    }
}
