package program.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс форматирования для дат
 */
public class DateUtil {
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Форматирование даты в строку
     * @param date - дата
     * @return строка даты
     */
    public static String format(LocalDate date){
        if (date == null){
            return null;
        }
        return DATE_TIME_FORMATTER.format(date);
    }

    /**
     * Форматировние строки в дату
     * @param dateString - строка даты
     * @return дата
     */
    public static LocalDate parse(String dateString){
        try{
            return DATE_TIME_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e){
            return null;
        }
    }

}
