package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.time.format.DateTimeFormatter;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        DateTimeParser parserDate = new SqlRuDateTimeParser();
        String urlPages = "https://www.sql.ru/forum/job-offers/";
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect(urlPages + i).get();
            Elements row = doc.select(".postslisttopic");
            Elements data = doc.select(".altCol");
            int index = 1;
            for (Element td : row) {
                Element href = td.child(0);
                Element date = data.get(index);
                System.out.printf("Описание: %s%nСсылка: %s%nДата добавления: %s%n",
                        href.text(),
                        href.attr("href"),
                        parserDate.parse(date.text()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                );
                System.out.println("----------------------------------------------------------------------");
                index += 2;
            }
        }
    }
}
