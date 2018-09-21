/*
 * Copyright (C) 2018 Dmitry Davletbaev <ddomgn@gmail.com>
 *
 * This file is part of rsscat.
 *
 * rsscat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * rsscat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rsscat.  If not, see <https://www.gnu.org/licenses/>.
 */
package ddomgn.rsscat;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RssChannel {

    static class Dates {
        final ZonedDateTime pubDate;
        final ZonedDateTime lastBuildDate;
        Dates(ZonedDateTime pubDate, ZonedDateTime lastBuildDate) {
            this.pubDate = pubDate;
            this.lastBuildDate = lastBuildDate;
        }
    }

    static class Details {
        final String language;
        final String docs;
        final String generator;
        final String managingEditor;
        final String webMaster;
        Details(String language, String docs, String generator, String managingEditor, String webMaster) {
            this.language = language;
            this.docs = docs;
            this.generator = generator;
            this.managingEditor = managingEditor;
            this.webMaster = webMaster;
        }
    }

    public final String title;
    public final String link;
    public final String description;
    final Dates dates;
    final Details details;

    private final List<RssItem> items;

    RssChannel(String title, String link, String description, Dates dates, Details details, List<RssItem> items) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.dates = dates;
        this.details = details;
        this.items = items;
    }

    List<RssItem> items(Settings settings) {
        var now = ZonedDateTime.now();
        Predicate<RssItem> inSelectedTime = item -> (item.pubDate == null ? now : item.pubDate).isAfter(now.minusDays(settings.lastDays));
        return items.stream().filter(inSelectedTime).collect(Collectors.toList());
    }

    boolean shouldBeShown(Settings settings) { return items(settings).size() > 0 || settings.showEmptyFeeds; }
}
