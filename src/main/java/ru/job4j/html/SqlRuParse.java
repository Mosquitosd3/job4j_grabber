package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    DateTimeParser parserDate;

    public SqlRuParse(DateTimeParser parserDate) {
        this.parserDate = parserDate;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                list.add(detail(td.child(0).attr("href")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post detail(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String link = url;
        String name = doc.selectFirst(".messageHeader").text();
        String text = doc.select(".msgBody").get(1).text();
        String formatDate = doc.select(".msgFooter").text();
        String date = formatDate.substring(0, formatDate.indexOf("[")).trim();
        return new Post(name, text, link, parserDate.parse(date));
    }
}
