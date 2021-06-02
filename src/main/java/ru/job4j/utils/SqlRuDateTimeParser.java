package ru.job4j.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;


public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, String> MONTHS = new HashMap<>() {
        {
            put("янв", "Jan");
            put("фев", "Feb");
            put("мар", "Mar");
            put("апр", "Apr");
            put("май", "May");
            put("июн", "Jun");
            put("июл", "Jul");
            put("авг", "Aug");
            put("сен", "Sep");
            put("окт", "Oct");
            put("ноя", "Nov");
            put("дек", "Dec");
        }
    };

    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String[] text = parse.split(",");
        String timeText = text[1].substring(1);
        if (parse.contains("сегодня")) {
            return getToday(timeText, timeFormatter);
        } else if (parse.contains("вчера")) {
            return getYesterday(timeText, timeFormatter);
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM yy", Locale.ENGLISH);
        StringJoiner joiner = new StringJoiner(" ");
        String[] date = text[0].split(" ");
        String day = date[0];
        String month = MONTHS.get(date[1]);
        String year = date[2];
        String dateText = joiner.add(day).add(month).add(year).toString();
        return LocalDateTime.of(LocalDate.parse(dateText, dateFormatter), LocalTime.parse(timeText, timeFormatter));
    }

    private LocalDateTime getToday(String time, DateTimeFormatter formatter) {
        return LocalDateTime.of(LocalDate.now(), LocalTime.parse(time, formatter));
    }

    private LocalDateTime getYesterday(String time, DateTimeFormatter formatter) {
        return LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.parse(time, formatter));
    }
}
