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
import java.util.Optional;

public class RssItem {

    public final String title;
    public final String link;
    public final String description;
    public final Optional<ZonedDateTime> pubDate;
    public final Optional<String> guid;

    RssItem(String title, String link, String description, Optional<ZonedDateTime> pubDate, Optional<String> guid) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.guid = guid;
    }
}
