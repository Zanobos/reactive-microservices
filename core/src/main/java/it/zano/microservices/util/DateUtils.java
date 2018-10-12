package it.zano.microservices.util;


import it.zano.microservices.exception.MicroServiceException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class DateUtils {


    private DateUtils() {}

    private static final String FORMAT_SHORT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER_SHORT = DateTimeFormatter.ofPattern(FORMAT_SHORT);
    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);
    private static final String FORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    private static final String FORMAT_FULL_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final DateTimeFormatter FORMATTER_FULL = DateTimeFormatter.ofPattern(FORMAT_FULL);
    private static final DateFormat FORMATTER_DATE;
    private static final DateFormat FORMATTER_DATE_SHORT;
    private static final DateFormat FORMATTER_DATE_Z;

    static {
        FORMATTER_DATE = new SimpleDateFormat(FORMAT_FULL); //For date to string
        FORMATTER_DATE_SHORT = new SimpleDateFormat(FORMAT_SHORT); //For date to short string (only year month day)
        FORMATTER_DATE_Z = new SimpleDateFormat(FORMAT_FULL_Z);
    }

    // String --> LocalDate
    public static LocalDate parseStringToLocalDate(String dateAsString) {
        return LocalDate.parse(dateAsString, FORMATTER_SHORT);
    }

    // String <-- LocalDate
    public static String formatDateToString(LocalDate date) {
        return FORMATTER_SHORT.format(date);
    }

    // String --> LocalDateTime
    public static LocalDateTime parseStringToLocalDateTime(String dateTimeAsString) {
        return LocalDateTime.parse(dateTimeAsString, FORMATTER_FULL);
    }

    // String <-- LocalDateTime
    public static String formatDateToString(LocalDateTime date) {
        return FORMATTER.format(date);
    }

    // String <-- OffsetDateTime
    public static String formatDateToString(OffsetDateTime date) {
        return FORMATTER_FULL.format(date);
    }

    // String --> Date
    public static Date parseStringToDate(String dateAsString) throws MicroServiceException {
        try {
            return FORMATTER_DATE.parse(dateAsString);
        } catch (ParseException e) {
            throw new MicroServiceException("Can not parse: " + dateAsString + " with format " + FORMAT_FULL);
        }
    }

    // String <-- Date
    public static String formatDateToString(Date date) {
        return FORMATTER_DATE.format(date);
    }

    // (short) String <-- Date, discard hour and keep only year month day
    public static String formatDateToDay(Date date) {
        return FORMATTER_DATE_SHORT.format(date);
    }

    // (short) Date <-- String, discard hour and keep only year month day
    public static Date parseStringToDay(String dateAsString) throws MicroServiceException {
        try {
            return FORMATTER_DATE_SHORT.parse(dateAsString);
        } catch (ParseException e) {
            throw new MicroServiceException("Can not parse: " + dateAsString + " with format " + FORMAT_SHORT);
        }
    }

    // (short) long <-- String, with value as yyyy-MM-dd
    public static long parseStringOfDateToTimestampInUtcZone(String dateAsString) throws MicroServiceException {
        LocalDate localDate = DateUtils.parseStringToLocalDate(dateAsString);
        return localDate.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public static String formatDateZToString(Date date) {
        return FORMATTER_DATE_Z.format(date);
    }
}
