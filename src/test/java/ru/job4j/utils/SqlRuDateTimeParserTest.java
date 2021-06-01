package ru.job4j.utils;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SqlRuDateTimeParserTest {
    SqlRuDateTimeParser parser = new SqlRuDateTimeParser();

    @Test
    public void whenDateToday() {
        String date = "сегодня, 21:47";
        String expected = "02 06 21 21:47";
        String out = parser.parse(date).format(DateTimeFormatter.ofPattern("dd MM yy HH:mm"));
        assertThat(expected, is(out));
    }

    @Test
    public void whenDateYesterday() {
        String date = "вчера, 23:59";
        String expected = "01 06 21 23:59";
        String out = parser.parse(date).format(DateTimeFormatter.ofPattern("dd MM yy HH:mm"));
        assertThat(expected, is(out));
    }

    @Test
    public void whenParseDate() {
        String date = "18 май 21, 15:49";
        String expected = "18 05 21 15:49";
        String out = parser.parse(date).format(DateTimeFormatter.ofPattern("dd MM yy HH:mm"));
        assertThat(expected, is(out));
    }
}