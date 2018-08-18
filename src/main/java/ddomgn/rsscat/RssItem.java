package ddomgn.rsscat;

import java.time.ZonedDateTime;

public class RssItem {
    public final String title;
    public final String link;
    public final String description;
    public final ZonedDateTime pubDate;
    public final String guid;

    public RssItem(String title, String link, String description, ZonedDateTime pubDate, String guid) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.guid = guid;
    }
}
