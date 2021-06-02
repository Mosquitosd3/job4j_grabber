package ru.job4j.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SqlRuDateTimeParserTest {
    SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MM yy HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Test
    public void whenDateToday() {
        String date = "сегодня, 21:47";
        String expected = LocalDateTime.of(LocalDate.now(), LocalTime.parse("21:47", timeFormatter)).format(dateFormatter);
        String out = parser.parse(date).format(dateFormatter);
        assertThat(expected, is(out));
    }

    @Test
    public void whenDateYesterday() {
        String date = "вчера, 23:59";
        String expected = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.parse("23:59", timeFormatter)).format(dateFormatter);
        String out = parser.parse(date).format(dateFormatter);
        assertThat(expected, is(out));
    }

    @Test
    public void whenParseDate() {
        String date = "18 май 21, 15:49";
        String expected = "18 05 21 15:49";
        String out = parser.parse(date).format(dateFormatter);
        assertThat(expected, is(out));
    }
}