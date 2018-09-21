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

class Printer {

    static final String INDENT = "    ";

    static String printChannel(RssChannel channel, Settings settings) {
        return channel.title
                + ((channel.description == null || settings.hideFeedDescription) ? "" : ": " + channel.description) + "\n"
                + channel.items(settings).stream().map(Printer::printItem).reduce((a, b) -> a + b).orElse("");
    }

    private static String printItem(RssItem item) {
        return INDENT + item.title + "\n"
                + INDENT + INDENT + item.link + "\n"
                + (item.pubDate == null ? "" : INDENT + INDENT + item.pubDate + "\n");
    }
}

