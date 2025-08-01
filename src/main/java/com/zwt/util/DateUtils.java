package com.zwt.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtils {

    public static String yyyy_MM_dd_HH_mm_ss="yyyy-MM-dd HH:mm:ss";

    public static final String DT_DATE_FORMAT = "yyyyMMdd";

    private static final FastDateFormat dtFormatter = FastDateFormat.getInstance(DT_DATE_FORMAT);


    public static String getTodayDt() {
        return dtFormatter.format(new Date());
    }

    public static String getDt(Date date) {
        return dtFormatter.format(date);
    }

    public static String formatTime(LocalDateTime time) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyyMMdd");
        return time.format(pattern);
    }

    public static String formatTime(LocalDateTime time, String patternString) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(patternString);
        return time.format(pattern);
    }

    public static String mmLongTimeToStringTime(Long longtime){
        if (longtime == null || longtime.longValue() <= 0){
            return StringUtils.EMPTY;
        }
        Date date=new Date(longtime);
        return DateFormatUtils.format(date,yyyy_MM_dd_HH_mm_ss);
    }

    public static String mmLongTimeToStringTime(Long longtime, String format){
        if (longtime == null || longtime.longValue() <= 0){
            return StringUtils.EMPTY;
        }
        Date date = new Date(longtime);
        return DateFormatUtils.format(date,format);
    }

    public static Date getDateByString(String dateStr, String format) {
        Date date = null;
        if (StringUtils.isNotBlank(dateStr)) {
            try {
                date = (new SimpleDateFormat(format)).parse(dateStr);
            } catch (ParseException e) {
                log.warn("SaasDateUtil.getDateByString() error. dateString = " + dateStr, e);
            }
        }
        return date;
    }

    /**
     * 获取当前时间毫秒时间戳
     *
     * @return
     */
    public static long getCurrTimestampOfMS() {
        return System.currentTimeMillis();
    }

    public static Long getStartTimeOfDt(Integer dt) throws ParseException {
        Date date = dtFormatter.parse(String.valueOf(dt));
        return date.getTime();
    }

    public static Long getEndTimeOfDt(Integer dt) throws ParseException {
        Date date = dtFormatter.parse(String.valueOf(dt));
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        instance.set(Calendar.MILLISECOND, 59);
        return instance.getTimeInMillis();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getStartTimeOfDt(20240528));
        System.out.println(getEndTimeOfDt(20240528));
    }
}
