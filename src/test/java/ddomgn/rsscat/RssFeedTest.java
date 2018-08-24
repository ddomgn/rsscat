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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RssFeedTest {

    @Test
    @DisplayName("RSS 2 feed test")
    public void testRss2Feed() throws Exception {
        URL url = RssFeedTest.class.getResource("/sample-rss-2.xml");
        RssFeed feed = new RssFeed(url);

        RssChannel channel = feed.read();
        assertEquals("Liftoff News", channel.title);
        assertEquals("http://liftoff.msfc.nasa.gov/", channel.link);
        assertEquals("Liftoff to Space Exploration.", channel.description);
        assertEquals("en-us", channel.language);
        assertEquals(ZonedDateTime.parse("2003-06-10T04:00:00+00:00"), channel.pubDate);
        assertEquals(ZonedDateTime.parse("2003-06-10T09:41:01+00:00"), channel.lastBuildDate);
        assertEquals("http://blogs.law.harvard.edu/tech/rss", channel.docs);
        assertEquals("Weblog Editor 2.0", channel.generator);
        assertEquals("editor@example.com", channel.managingEditor);
        assertEquals("webmaster@example.com", channel.webMaster);

        assertEquals(4, channel.items.size());
        assertEquals("Star City", channel.items.get(0).title);
        assertEquals("http://liftoff.msfc.nasa.gov/news/2003/news-starcity.asp", channel.items.get(0).link);
        assertEquals("How do Americans get ready to work with Russians aboard the International Space Station? "
                + "They take a crash course in culture, language and protocol at Russia's "
                + "<a href=\"http://howe.iki.rssi.ru/GCTC/gctc_e.htm\">Star City</a>.",
                channel.items.get(0).description);
        assertEquals(ZonedDateTime.parse("2003-06-03T09:39:21+00:00"), channel.items.get(0).pubDate);
        assertEquals("http://liftoff.msfc.nasa.gov/2003/06/03.html#item573", channel.items.get(0).guid);
    }
}
