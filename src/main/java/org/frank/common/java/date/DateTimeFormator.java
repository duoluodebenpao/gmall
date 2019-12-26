package org.frank.common.java.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeFormator {
    private static final Logger logger = LoggerFactory.getLogger(DateTimeFormator.class);
    public static String displayFormat = "yyyy-MM-dd HH:mm:ss";
    public static String hwDateFormat = "yyyy/MM/dd HH:mm:ss";
    public static String shortFormat = "HH:mm:ss";
    public static String dayFormat = "yyyy-MM-dd";
    public static SimpleDateFormat normalFormator = new SimpleDateFormat(displayFormat);
    public static SimpleDateFormat hwFormator = new SimpleDateFormat(hwDateFormat);
    public static SimpleDateFormat shortFormator = new SimpleDateFormat(shortFormat);
    public static SimpleDateFormat dayFormator = new SimpleDateFormat(dayFormat);

    public static String secondFormat = "yyyy-MM-dd HH:mm:ss";

    public static String minuteFormat = "yyyy-MM-dd HH:mm";

    public static String hourFormat = "yyyy-MM-dd HH";

    public static String monthFormat = "yyyy-MM";

    public static String yearFormat = "yyyy";

    public static String normalFormat(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return normalFormator.format(timestamp);
    }

    public static String shortFormat(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return shortFormator.format(timestamp);
    }

    public static String dayFormat(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return dayFormator.format(timestamp);
    }

    public static DateTime fromFormatStr(String dateTimeStr) {
        return fromFormatStr(dateTimeStr, displayFormat);
    }

    public static DateTime fromFormatStr(String dateTimeStr, String formatter) {
        if (null == dateTimeStr || "".equals(dateTimeStr)) {
            return null;
        }
        DateTimeFormatter format = DateTimeFormat.forPattern(formatter);
        DateTime dateTime = format.parseDateTime(dateTimeStr);

        return dateTime;
    }

    public static long parseDateTime2Unixtimestamp(String dateTime) {
        Date date = null;
        try {
            date = normalFormator.parse(dateTime);
            return date.getTime() / 1000L;
        } catch (ParseException e) {
            logger.error("parseDateTime2Unixtimestamp err, dataTime={}", dateTime);
        }

        return -1L;
    }

    public static String UTCStringToDefaultString(String UTCString) {
        try {
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            defaultFormat.setTimeZone(TimeZone.getDefault());
            Date date = utcFormat.parse(UTCString);
            return defaultFormat.format(date);
        } catch (ParseException pe) {
            logger.error("parse error : {}", pe);
            return "";
        }
    }

    public static String dateTimeToUTCString(String dateTime) {
        try {
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            defaultFormat.setTimeZone(TimeZone.getDefault());
            Date date = defaultFormat.parse(dateTime);
            return utcFormat.format(date);
        } catch (ParseException pe) {
            logger.error("parse error : {}", pe);
            return "";
        }
    }

    public static long parseHwDateTime2Unixtimestamp(String dateTime) {
        Date date = null;
        try {
            date = hwFormator.parse(dateTime);
            return date.getTime() / 1000L;
        } catch (ParseException e) {
            logger.error("parseDateTime2Unixtimestamp err, dataTime={}", dateTime);
        }

        return -1L;
    }

    public static String timeStamp2normal(Long unixSeconds) {
        if (null == unixSeconds) {
            return "";
        }
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        String formattedDate = sdf.format(date);
        return formattedDate.toString();
    }

    public static Timestamp parseDateTimeStr2Timestamp(String dateTime) {
        if (dateTime == null || dateTime == "") return null;
        Date date = null;
        try {
            date = normalFormator.parse(dateTime);
        } catch (ParseException e) {
            logger.error("parseDateTimeStr2Timestamp err, dataTime={}", dateTime);
        }
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }
}
