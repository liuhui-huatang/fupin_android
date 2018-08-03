package com.huatang.fupin.utils;

/**
 * Created by forever on 16/12/21.
 */

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期操作工具类.
 *
 * @version 1.0
 * @Project ERPForAndroid
 */

@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static final String FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");




    /**
     * 获得当前时间的毫秒数
     * <p>
     * 详见{@link System#currentTimeMillis()}
     *
     * @return
     */
    public static long getMillis() {
        return System.currentTimeMillis();
    }


    /**
     * 把字符串转化为时间格式
     *
     * @param timestamp
     * @return
     */
    public static String getStandardTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(timestamp * 1000);
        sdf.format(date);
        return sdf.format(date);
    }

    /**
     * 获得当前日期时间 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String currentDatetime() {
        return datetimeFormat.format(now());
    }



    //字符串转时间戳
    public static String strToTime(String timeString) {
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        Date d;
        try {
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    //时间戳转字符串
    public static String timeToStr(String timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    /**
     * 字符串转时间
     *
     * @param str
     * @param format
     * @return
     */
    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }


    /**
     * 转化时间输入时间与当前时间的间隔
     *
     * @param timestamp
     * @return
     */
    public static int converTime(long timestamp) {
        long currentSeconds = System.currentTimeMillis();
        long timeGap = (currentSeconds - timestamp) / 1000;// 与现在时间相差毫秒数
        int timeStr = 0;
//        if (timeGap > 24 * 60 * 60) {// 1天以上
//            timeStr = timeGap / (24 * 60 * 60) + "天前";
//        } else if (timeGap > 60 * 60) {// 1小时-24小时
//            timeStr = timeGap / (60 * 60) + "小时前";
//        } else if (timeGap > 60) {// 1分钟-59分钟
//            timeStr = timeGap / 60 + "分钟前";
//        } else {// 1秒钟-59秒钟
//            timeStr = "刚刚";
//        }

        if (timeGap > 24 * 60 * 60) {// 1天以上
            timeStr = (int) (timeGap / (24 * 60 * 60 ));
        } else {
            timeStr = 1;
        }
        return timeStr;
    }

    /**
     * 比较两个时间戳相差的天数
     *
     * @return
     */
    public static int converTime(long starttime, long endtime) {
        long timeGap = (endtime - starttime) / 1000;// 与现在时间相差秒数
        int timeStr = 0;
//        if (timeGap > 24 * 60 * 60) {// 1天以上
//            timeStr = timeGap / (24 * 60 * 60) + "天前";
//        } else if (timeGap > 60 * 60) {// 1小时-24小时
//            timeStr = timeGap / (60 * 60) + "小时前";
//        } else if (timeGap > 60) {// 1分钟-59分钟
//            timeStr = timeGap / 60 + "分钟前";
//        } else {// 1秒钟-59秒钟
//            timeStr = "刚刚";
//        }

        if (timeGap > 24 * 60 * 60) {// 1天以上
            timeStr = (int) timeGap / (24 * 60 * 60);
        } else {
            timeStr = 1;
        }
        return timeStr;
    }


    /**
     * 格式化日期时间 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatDatetime(Date date) {
        return datetimeFormat.format(date);
    }

    /**
     * 获得当前时间 时间格式HH:mm:ss
     *
     * @return
     */
    public static String currentTime() {
        return timeFormat.format(now());
    }

    /**
     * 格式化时间 时间格式HH:mm:ss
     *
     * @return
     */
    public static String formatTime(Date date) {
        return timeFormat.format(date);
    }

    /**
     * 获得当前时间的java.util.Date对象
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }


    /**
     * 获得当前Chinese月份
     *
     * @return
     */
    public static int month() {
        return calendar().get(Calendar.MONTH) + 1;
    }

    /**
     * 获得月份中的第几天
     *
     * @return
     */
    public static int dayOfMonth() {
        return calendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 今天是星期的第几天
     *
     * @return
     */
    public static int dayOfWeek() {
        return calendar().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 今天是年中的第几天
     *
     * @return
     */
    public static int dayOfYear() {
        return calendar().get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 判断原日期是否在目标日期之前
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    /**
     * 判断原日期是否在目标日期之后
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    /**
     * 判断两日期是否相同
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * 判断某个日期是否在某个日期范围
     *
     * @param beginDate 日期范围开始
     * @param endDate   日期范围结束
     * @param src       需要判断的日期
     * @return
     */
    public static boolean between(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }

    /**
     * 获得当前月的最后一天
     * <p>
     * HH:mm:ss为0，毫秒为999
     *
     * @return
     */
    public static Date lastDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// 月份+1
        cal.set(Calendar.MILLISECOND, -1);// 毫秒-1
        return cal.getTime();
    }

    /**
     * 获得当前月的第一天
     * <p>
     * HH:mm:ss SS为零
     *
     * @return
     */
    public static Date firstDayOfMonth() {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
        cal.set(Calendar.MINUTE, 0);// m置零
        cal.set(Calendar.SECOND, 0);// s置零
        cal.set(Calendar.MILLISECOND, 0);// S置零
        return cal.getTime();
    }

    private static Date weekDay(int week) {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_WEEK, week);
        return cal.getTime();
    }

    /**
     * 获得周五日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date friday() {
        return weekDay(Calendar.FRIDAY);
    }

    /**
     * 获得周六日期
     * <p>
     * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date saturday() {
        return weekDay(Calendar.SATURDAY);
    }

    /**
     * 获得周日日期 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
     *
     * @return
     */
    public static Date sunday() {
        return weekDay(Calendar.SUNDAY);
    }

    /**
     * 将字符串日期时间转换成java.util.Date类型 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param datetime
     * @return
     */
    public static Date parseDatetime(String datetime) throws ParseException {
        return datetimeFormat.parse(datetime);
    }

    /**
     * 将字符串日期转换成java.util.Date类型 日期时间格式yyyy-MM-dd
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    /**
     * 将字符串日期转换成java.util.Date类型 时间格式 HH:mm:ss
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date parseTime(String time) throws ParseException {
        return timeFormat.parse(time);
    }

    /**
     * 根据自定义pattern将字符串日期转换成java.util.Date类型
     *
     * @param datetime
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDatetime(String datetime, String pattern) throws ParseException {
        SimpleDateFormat format = (SimpleDateFormat) datetimeFormat.clone();
        format.applyPattern(pattern);
        return format.parse(datetime);
    }

    /**
     * 把秒格式化为分种小时
     *
     * @param second
     * @return
     */
    public static String parseSecond(int second) {
        if (second >= 60) {
            return second / 60 + "分";
        } else if (second >= 60 * 60) {
            return second / 60 * 60 + "时";
        } else if (second >= 60 * 60 * 24) {
            return second / 60 * 60 + "天";
        } else {
            return second + "秒";
        }
    }


    /**
     * 比较时间大小
     *
     * @param begin
     * @param end
     * @return
     */
    public static int compareDate(String begin, String end) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date beginDate = df.parse(begin);
            Date endDate = df.parse(end);
            if (beginDate.getTime() < endDate.getTime()) {
                return 1;
            } else if (beginDate.getTime() > endDate.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得年份
     *
     * @param
     * @return
     */
    public static int getYear() {
        Calendar c = Calendar.getInstance();

        return c.get(Calendar.YEAR);
    }


    /**
     * 获得月份
     *
     * @param
     * @return
     */
    public static int getMonth() {
        Calendar c = Calendar.getInstance();

        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取季度
     */
    public static String getJiDu() {
        String jidu = "一";
        switch (getMonth()) {
            case 1:
            case 2:
            case 3:
                jidu = "一";
                break;
            case 4:
            case 5:
            case 6:
                jidu = "二";
                break;
            case 7:
            case 8:
            case 9:
                jidu = "三";
                break;
            case 10:
            case 11:
            case 12:
                jidu = "四";
                break;
        }
        return jidu;
    }

    /**
     * 获得星期几
     *
     * @param date
     * @return
     */
    public int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获得日期
     *
     * @param date
     * @return
     */
    public int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }

    /**
     * 获得天数差
     *
     * @param begin
     * @param end
     * @return
     */
    public long getDayDiff(Date begin, Date end) {
        long day = 1;
        if (end.getTime() < begin.getTime()) {
            day = -1;
        } else if (end.getTime() == begin.getTime()) {
            day = 1;
        } else {
            day += (end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000);
        }
        return day;
    }
}
