package ddomgn.rsscat;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class RssChannel {
    public final String title;
    public final String link;
    public final String description;
    public final String language;
    public final ZonedDateTime pubDate;
    public final ZonedDateTime lastBuildDate;
    public final String docs;
    public final String generator;
    public final String managingEditor;
    public final String webMaster;
    public final List<RssItem> items;

    public RssChannel(String title, String link, String description, String language, ZonedDateTime pubDate,
                      ZonedDateTime lastBuildDate, String docs, String generator, String managingEditor,
                      String webMaster, List<RssItem> items) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.pubDate = pubDate;
        this.lastBuildDate = lastBuildDate;
        this.docs = docs;
        this.generator = generator;
        this.managingEditor = managingEditor;
        this.webMaster = webMaster;
        this.items = items;
    }
}
