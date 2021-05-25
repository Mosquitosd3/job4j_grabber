package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        Elements data = doc.select(".altCol");
        int index = 0;
        for (Element td : row) {
            Element href = td.child(0);
            Element date = data.get(index);
            System.out.printf("Описание: %s\nСсылка: %s\n", href.text(), href.attr("href"));
            System.out.printf("Дата добавления: %s\n", date.text());
            System.out.println("----------------------------------------------------------------------");
            index++;
        }
    }
}
