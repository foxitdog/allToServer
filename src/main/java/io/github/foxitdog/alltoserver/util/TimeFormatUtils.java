package io.github.foxitdog.alltoserver.util;

import lombok.extern.log4j.Log4j2;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式化
 */
@Log4j2
public enum TimeFormatUtils {
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	yMdHms_("yyyy-MM-dd HH:mm:ss"),
	/**
	 * yyyyMMddHHmmss
	 */
	yMdHms("yyyyMMddHHmmss"),
	/**
	 * yyyyMM
	 */
	yM("yyyyMM"),
	/**
	 * yyyyMM
	 */
	y_M("yyyy-MM"),
	/**
	 * HH:mm
	 */
	Hm("HH:mm"),
	/** 日期格式 <code>[yyyy-MM-dd]</code> */
	DATE("yyyy-MM-dd"),

	/** 日期格式 <code>[yyyyMMdd]</code> */
	DATE_COMPACT("yyyyMMdd"),

	/** 日期格式 <code>[yyyy_MM_dd]</code> */
	DATE_UNDERLINE("yyyy_MM_dd"),

	/** 时间格式 <code>[HH:mm:ss]</code> */
	TIME("HH:mm:ss"),

	/** 时间格式 <code>[HHmmss]</code> */
	TIME_COMPACT("HHmmss"),

	/** 时间格式 <code>[HH_mm_ss]</code> */
	TIME_UNDERLINE("HH_mm_ss"),

	/** 时间格式 <code>[HH:mm:ss.SSS]</code> */
	TIME_MILLI("HH:mm:ss.SSS"),

	/** 时间格式 <code>[HHmmssSSS]</code> */
	TIME_MILLI_COMPACT("HHmmssSSS"),

	/** 时间格式 <code>[HH_mm_ss_SSS]</code> */
	TIME_MILLI_UNDERLINE("HH_mm_ss_SSS"),

	/** 日期时间格式 <code>[yyyy-MM-dd HH:mm:ss]</code> */
	DATE_TIME("yyyy-MM-dd HH:mm:ss"),

	/** 日期时间格式 <code>[yyyyMMddHHmmss]</code> */
	DATE_TIME_COMPACT("yyyyMMddHHmmss"),

	/** 日期时间格式 <code>[yyyy_MM_dd_HH_mm_ss]</code> */
	DATE_TIME_UNDERLINE("yyyy_MM_dd_HH_mm_ss"),

	/** 日期时间格式 <code>[yyyy-MM-dd HH:mm:ss.SSS]</code> */
	DATE_TIME_MILLI("yyyy-MM-dd HH:mm:ss.SSS"),

	/** 日期时间格式 <code>[yyyyMMddHHmmssSSS]</code> */
	DATE_TIME_MILLI_COMPACT("yyyyMMddHHmmssSSS"),

	/** 日期时间格式 <code>[yyyy_MM_dd_HH_mm_ss_SSS]</code> */
	DATE_TIME_MILLI_UNDERLINE("yyyy_MM_dd_HH_mm_ss_SSS"),
	/** 日期时间格式 <code>[yyMMdd]</code> */
 	y2Md("yyMMdd");

	TimeFormatUtils(String fmt) {
		this.dtf = DateTimeFormatter.ofPattern(fmt);
	}

	private DateTimeFormatter dtf;

	public String now() {
		return LocalDateTime.now().format(dtf);
	}

	public String format(Date date) {
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		return ldt.format(dtf);
	}

	public String format(long date) {
		return format(new Date(date));
	}

	public Date parse(String date) {
		return Date.from(LocalDateTime.parse(date,dtf).toInstant(ZoneOffset.of("+8")));
	}

	/**
	 * Formats a date-time object using this formatter.
	 *
	 * @return the formatted string, not null
	 */
	public String formatNow() {
		return now();
	}

	/**
	 * Formats a date-time object using this formatter.
	 *
	 * @param epochMilli
	 * @return the formatted string, not null
	 */
	public String formatTimestamp(long epochMilli) {
		return format(epochMilli);
	}

	/**
	 * Formats a date-time object using this formatter.
	 *
	 * @param date
	 * @return the formatted string, not null
	 */
	public String formatDate(Date date) {
		return format(date);
	}

	// --------------------------------------------------------------------------------------------

	/**
	 * Fully parses the text producing an object of the specified type.
	 *
	 * @param date
	 * @return the parsed time-stamp
	 */
	public long asTimestamp(String date) {
		return parse(date).getTime();
	}

	/**
	 * Fully parses the text producing an object of the specified type.
	 *
	 * @param date
	 * @return the parsed date-time, not null
	 */
	public Date parseDate(String date) {
		return parse(date);
	}

	// --------------------------------------------------------------------------------------------

	/**
	 * Adds to a date returning a new object. The original date object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param calendarField
	 *            the calendar field to add to
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 */
	private static Date add(Date date, int calendarField, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	/**
	 * Adds a number of years to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addYears(Date date, int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	/**
	 * Adds a number of months to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addMonths(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	/**
	 * Adds a number of weeks to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addWeeks(Date date, int amount) {
		return add(date, Calendar.WEEK_OF_YEAR, amount);
	}

	/**
	 * Adds a number of days to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addDays(Date date, int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}

	/**
	 * Adds a number of hours to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addHours(Date date, int amount) {
		return add(date, Calendar.HOUR_OF_DAY, amount);
	}

	/**
	 * Adds a number of minutes to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addMinutes(Date date, int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	/**
	 * Adds a number of seconds to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addSeconds(Date date, int amount) {
		return add(date, Calendar.SECOND, amount);
	}

	/**
	 * Adds a number of milliseconds to a date returning a new object. The original
	 * date object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addMilliseconds(Date date, int amount) {
		return add(date, Calendar.MILLISECOND, amount);
	}

	// --------------------------------------------------------------------------------------------

	/**
	 * Adds a number of years to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public String addYears(String date, int amount) {
		return parse2LDT(date).plusYears(amount).format(dtf);
	}

	/**
	 * parse to LocalDateTime
	 * @param date
	 * @return
	 */
	private LocalDateTime parse2LDT(String date) {
		return LocalDateTime.parse(date,dtf);
	}

	/**
	 * Adds a number of months to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public String addMonths(String date, int amount) {
		return parse2LDT(date).plusMonths(amount).format(dtf);
	}

	/**
	 * Adds a number of weeks to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public String addWeeks(String date, int amount) {
		return parse2LDT(date).plusWeeks(amount).format(dtf);
	}

	/**
	 * Adds a number of days to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public String addDays(String date, int amount) {
		return parse2LDT(date).plusDays(amount).format(dtf);
	}

	/**
	 * Adds a number of hours to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public String addHours(String date, int amount) {
		return parse2LDT(date).plusHours(amount).format(dtf);
	}

	/**
	 * Adds a number of minutes to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public String addMinutes(String date, int amount) {
		return parse2LDT(date).plusMinutes(amount).format(dtf);
	}

	/**
	 * Adds a number of seconds to a date returning a new object. The original date
	 * object is unchanged.
	 *
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public String addSeconds(String date, int amount) {
		return parse2LDT(date).plusSeconds(amount).format(dtf);
	}
}
