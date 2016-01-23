package kr.co.hanbit.mastering.springmvc4.date;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

public class KRLocalDateFormatter implements Formatter<LocalDate> {
    private static final String KR_PATTERN = "yyyy/MM/dd";
    private static final String NORMAL_PATTERN = "dd/MM/yyyy";

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern(getPattern(locale)).format(object);
    }

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(getPattern(locale)));
    }

    public static String getPattern(Locale locale) {
        return isKorea(locale) ? KR_PATTERN : NORMAL_PATTERN;
    }

    private static boolean isKorea(Locale locale) {
        return Locale.KOREA.getCountry().equals(locale.getCountry());
    }
}
