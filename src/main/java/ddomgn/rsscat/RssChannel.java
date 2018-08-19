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

    RssChannel(String title, String link, String description, String language, ZonedDateTime pubDate,
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