package com.hanframework.kit.date;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 年-月-日 时:分:秒.毫秒->2019-11-05 17:43:29.383
 * 年-月-日 时:分:秒->2019-11-05 17:43:29
 * 时:分:秒->17:43:29
 * 年-月-日 时:分->2019-11-05 17:43
 * 年-月-日->2019-11-05
 * 年-月->2019-11
 * 年->2019
 * 月->11
 * 日->05
 * 时->17
 * 分->43
 * 秒->29
 * 中文格式年月日时分秒毫秒->2019年11月05日 17时43分29秒386毫秒
 * 中文格式年月日时分秒->2019年11月05日 17时43分29秒
 * 中文格式年月日->2019年11月05日
 * 中文格式年月->2019年11月
 * 中文格式年->2019年
 * 中文格式时分秒->17时43分29秒
 * 无间隔符的年月日时分秒->20191105174329
 * 无间隔符的年月日时分秒毫秒->20191105174329387
 * 无间隔符的年月日->20191105
 *
 * @author liuxin
 */
public enum DatePatternEnum {

    DATE_TIME_MS_PATTERN(0, "yyyy-MM-dd HH:mm:ss.SSS", "年-月-日 时:分:秒.毫秒"),

    DATE_TIME_PATTERN(1, "yyyy-MM-dd HH:mm:ss", "年-月-日 时:分:秒"),

    TIME_PATTERN(2, "HH:mm:ss", "时:分:秒"),

    MINUTE_PATTERN(3, "yyyy-MM-dd HH:mm", "年-月-日 时:分"),

    DATE_PATTERN(4, "yyyy-MM-dd", "年-月-日"),

    MONTH_PATTERN(5, "yyyy-MM", "年-月"),

    ONLY_YEAR_PATTERN(6, "yyyy", "年"),

    ONLY_MONTH_PATTERN(7, "MM", "月"),

    ONLY_DAY_PATTERN(8, "dd", "日"),

    ONLY_HOUR_PATTERN(9, "HH", "时"),

    ONLY_MINUTE_PATTERN(10, "mm", "分"),

    ONLY_SECOND_PATTERN(11, "ss", "秒"),

    ZN_DATE_TIME_MS_PATTERN(12, "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒", "中文格式年月日时分秒毫秒"),

    ZN_DATE_TIME_PATTERN(13, "yyyy年MM月dd日 HH时mm分ss秒", "中文格式年月日时分秒"),

    ZN_DATE_PATTERN(14, "yyyy年MM月dd日", "中文格式年月日"),

    ZN_MONTH_PATTERN(15, "yyyy年MM月", "中文格式年月"),

    ZN_YEAR_ONLY_PATTERN(16, "yyyy年", "中文格式年"),

    ZN_TIME_PATTERN(17, "HH时mm分ss秒", "中文格式时分秒"),

    GAP_LESS_DATE_TIME_PATTERN(18, "yyyyMMddHHmmss", "无间隔符的年月日时分秒"),

    GAP_LESS_DATE_TIME_MS_PATTERN(19, "yyyyMMddHHmmssSSS", "无间隔符的年月日时分秒毫秒"),

    GAP_LESS_DATE_PATTERN(20, "yyyyMMdd", "无间隔符的年月日");


    private int index;

    private String pattern;

    private String desc;

    private static final Map<DatePatternEnum, SimpleDateFormat> formatCache = new WeakHashMap<>(initialCapacity());

    private static final Map<DatePatternEnum, DateTimeFormatter> formatterCache = new WeakHashMap<>(initialCapacity());

    static {
        checkCache();
    }

    //一个数如果是奇数的话，那么他的二进制最后一位一定为1,如果为奇数+1返回
    private static int initialCapacity() {
        return (values().length & 1) == 1 ? values().length + 1 : values().length;
    }

    private static void checkCache() {
        if (formatCache.isEmpty() || formatCache.size() != values().length) {
            formatCache.clear();
            for (DatePatternEnum datePatternEnum : values()) {
                formatCache.put(datePatternEnum, new SimpleDateFormat(datePatternEnum.getPattern()));
            }
        }
        if (formatterCache.isEmpty() || formatterCache.size() != values().length) {
            formatterCache.clear();
            for (DatePatternEnum datePatternEnum : values()) {
                formatterCache.put(datePatternEnum, DateTimeFormatter.ofPattern(datePatternEnum.getPattern()));
            }
        }
    }

    DatePatternEnum(int index, String pattern, String desc) {
        this.index = index;
        this.pattern = pattern;
        this.desc = desc;
    }

    public int getIndex() {
        return index;
    }

    public String getPattern() {
        return pattern;
    }

    public String getDesc() {
        return desc;
    }

    public DateTimeFormatter getFormatter() {
        checkCache();
        return formatterCache.getOrDefault(this, DateTimeFormatter.ofPattern(getPattern()));
    }

    private SimpleDateFormat getUnSafeDateFormat() {
        checkCache();
        return formatCache.getOrDefault(this, new SimpleDateFormat(getPattern()));
    }

    public String format() {
        return LocalDateTime.now().format(getFormatter());
    }

    public String format(Date date) {
        assert date != null;
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(getFormatter());
    }

    public Date parse(String dateText) throws Exception {
        return getUnSafeDateFormat().parse(dateText);
    }

}