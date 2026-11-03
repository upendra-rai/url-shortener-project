package com.upendra.url.service.util;

import static java.time.ZoneId.of;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.upendra.url.service.exceptions.ServiceException;

//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

	private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd h:mm a";
	private static final String DEFAULT_TIME_FORMAT = "h:mm a";
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String FORM_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String DEFAULT_TIMEZONE = "UTC";
	public static final String DISPLAY_DATE_TIME_FORMAT = "MMM dd, yyyy hh:mm a zzz";

	public static String convertToString(LocalDateTime date, String pattern) {
		if (isNull(date)) {
			return null;
		}
		DateTimeFormatter formatter = ofPattern(defaultIfBlank(pattern, DEFAULT_DATE_TIME_FORMAT));
		String strDate = formatter.format(date);
		return strDate.replace(" ", "T");
	}

	public static String convertToString(LocalDateTime date, String pattern, String timeZone) {
		if (date == null) {
			return null;
		}
		ZonedDateTime inputZonedDateTime = date.atZone(systemDefault());
		ZonedDateTime outputZonedDateTime = inputZonedDateTime
				.withZoneSameInstant(of(defaultIfBlank(timeZone, DEFAULT_TIMEZONE)));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultIfBlank(pattern, DEFAULT_DATE_TIME_FORMAT));
		String dateStr = formatter.format(outputZonedDateTime);
		return (isNotBlank(pattern) && pattern.equals(FORM_DATE_TIME_FORMAT)) ? dateStr.replace(" ", "T") : dateStr;
	}

	public static LocalTime convertStringToLocalTime(String time, String pattern) {
		if (isBlank(time)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultIfBlank(pattern, DEFAULT_TIME_FORMAT));
		return LocalTime.parse(time, formatter);
	}

	public static String convertToString(LocalTime time, String pattern) {
		if (isNull(time)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultIfBlank(pattern, DEFAULT_TIME_FORMAT));
		return formatter.format(time);
	}

	public static LocalDateTime convertStringToLocalDateTime(String strDate, String pattern) {
		if (isBlank(strDate)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultIfBlank(pattern, DEFAULT_DATE_TIME_FORMAT));
		return LocalDateTime.parse(strDate.replace("T", " "), formatter);
	}

	public static LocalDateTime convertStringToLocalDateTime(String strDate, String pattern, String timeZone) {
		LocalDateTime localDateTime = convertStringToLocalDateTime(strDate, pattern);
		if (isNull(localDateTime)) {
			return null;
		}
		return isNotBlank(timeZone)
				? localDateTime.atZone(ZoneId.of(timeZone)).withZoneSameInstant(ZoneId.systemDefault())
						.toLocalDateTime()
				: localDateTime;
	}

	public static ZonedDateTime convertStringToZonedDateTime(String strDate, String pattern, String timeZone) {
		try {
			LocalDateTime localDateTime = convertStringToLocalDateTime(strDate, pattern);
			if (isNull(localDateTime)) {
				return null;
			}
			return isNotBlank(timeZone)
					? localDateTime.atZone(ZoneId.of(timeZone)).withZoneSameInstant(ZoneId.of("UTC"))
					: localDateTime.atZone(ZoneId.of("UTC"));
		} catch (Exception e) {
			throw new ServiceException("Provided date time is invalid. Valid format is : {0}" + pattern);
		}
	}

	public static LocalDateTime getStartDateTime(String date, String dateTimeFormat, String timeZone) {
		if (isBlank(dateTimeFormat)) {
			dateTimeFormat = FORM_DATE_TIME_FORMAT;
		}
		if (isBlank(date)) {
			return null;
		}
		String dateTime = date.concat(" 00:00");
		if (isBlank(timeZone)) {
			return convertStringToLocalDateTime(dateTime, dateTimeFormat);
		}
		ZonedDateTime startDateTime = convertStringIntoLocalDateTime(dateTime, dateTimeFormat, ZoneId.of(timeZone),
				ZoneId.systemDefault());
		if (startDateTime != null) {
			return startDateTime.toLocalDateTime();
		}
		return null;
	}

	public static LocalDateTime getEndDateTime(String date, String dateTimeFormat, String timeZone) {
		if (isBlank(dateTimeFormat)) {
			dateTimeFormat = FORM_DATE_TIME_FORMAT;
		}
		if (isBlank(date)) {
			return null;
		}
		String dateTime = date.concat(" 23:59");
		if (isBlank(timeZone)) {
			return convertStringToLocalDateTime(dateTime, dateTimeFormat);
		}
		ZonedDateTime startDateTime = convertStringIntoLocalDateTime(dateTime, dateTimeFormat, ZoneId.of(timeZone),
				ZoneId.systemDefault());
		if (startDateTime != null) {
			return startDateTime.toLocalDateTime();
		}
		return null;
	}

	public static ZonedDateTime convertStringIntoLocalDateTime(String dateStr, String pattern, ZoneId fromZone,
			ZoneId toZone) {
		if (isBlank(dateStr)) {
			return null;
		}
		dateStr = dateStr.replace("T", " ");
		String formatPattern = defaultIfBlank(pattern, DEFAULT_DATE_TIME_FORMAT);
		LocalDateTime ldt = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(formatPattern));
		ZonedDateTime dateTimeInFromZoneId = ldt.atZone(fromZone);
		return dateTimeInFromZoneId.withZoneSameInstant(toZone);
	}

	public static String convertToString(LocalDate date, String pattern) {
		if (isNull(date)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultIfBlank(pattern, DEFAULT_DATE_FORMAT));
		return formatter.format(date);
	}

	public static LocalDate convertStringToLocalDate(String date, String pattern) {
		if (isBlank(date)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultIfBlank(pattern, DEFAULT_DATE_FORMAT));
		return LocalDate.parse(date, formatter);
	}

	public static String convertZonedDateTimeToString(ZonedDateTime zonedDateTime, String pattern, String toTimeZone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultIfBlank(pattern, DISPLAY_DATE_TIME_FORMAT));
		return formatter.format(zonedDateTime.withZoneSameInstant(ZoneId.of(toTimeZone)));
	}

	public static String convertDateFormat(String dateString, String inputFormat, String outputFormat) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
		LocalDate date = LocalDate.parse(dateString, inputFormatter);

		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
		return outputFormatter.format(date);
	}

	public static ZonedDateTime convertToSystemZonedDateTime(LocalDate localDate, LocalTime localTime,
			String timeZone) {
		if (nonNull(localDate) && nonNull(localTime)) {
			ZonedDateTime sourceTime = ZonedDateTime.of(localDate, localTime, ZoneId.of(timeZone));
			return sourceTime.withZoneSameInstant(ZoneId.systemDefault());
		} else {
			return ZonedDateTime.now();
		}
	}

	public static ZonedDateTime convertLongToZonedDateTime(Long epochSeconds) {
		Instant i = Instant.ofEpochSecond(epochSeconds);
		return ZonedDateTime.ofInstant(i, ZoneId.of("UTC"));
	}

}
