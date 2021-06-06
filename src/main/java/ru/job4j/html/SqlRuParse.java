package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        String urlPages = "https://www.sql.ru/forum/job-offers/";
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect(urlPages + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Post post = detail(td.child(0).attr("href"));
                System.out.printf("Name: %s%nLink: %s%nText: %s%nDate: %s%n",
                        post.getName(),
                        post.getLink(),
                        post.getText(),
                        post.getDate().format(DateTimeFormatter.ofPattern("dd MMM yy, HH:mm")));
            }
        }
    }

    private static Post detail(String url) throws IOException {
        DateTimeParser parserDate = new SqlRuDateTimeParser();
        Document doc = Jsoup.connect(url).get();
        String link = url;
        String name = doc.selectFirst(".messageHeader").text();
        String text = doc.select(".msgBody").get(1).text();
        String formatDate = doc.select(".msgFooter").text();
        String date = formatDate.substring(0, formatDate.indexOf("[")).trim();
        return new Post(name, text, link, parserDate.parse(date));
    }
}
