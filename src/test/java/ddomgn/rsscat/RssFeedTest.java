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
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RssFeedTest {

    private final Settings defaultSettings = new Settings();

    @Test
    @DisplayName("RSS 1 feed test")
    public void testRss1Feed() throws Exception {
        URL url = RssFeedTest.class.getResource("/sample-rss-1.xml");
        RssFeed feed = new RssFeed(url);

        RssChannel channel = feed.read();
        assertEquals("XML.com", channel.title);
        assertEquals("http://xml.com/pub", channel.link);
        assertEquals("XML.com features a rich mix of information and services for the XML community.", channel.description);
        assertNull(channel.language);
        assertNull(channel.pubDate);
        assertNull(channel.lastBuildDate);
        assertNull(channel.docs);
        assertNull(channel.generator);
        assertNull(channel.managingEditor);
        assertNull(channel.webMaster);

        assertEquals(2, channel.items(defaultSettings).size());
        assertEquals("Processing Inclusions with XSLT", channel.items(defaultSettings).get(0).title);
        assertEquals("http://xml.com/pub/2000/08/09/xslt/xslt.html", channel.items(defaultSettings).get(0).link);
        assertEquals("Processing document inclusions with general XML tools can be problematic. This article proposes "
                + "a way of preserving inclusion information through SAX-based processing.",
                channel.items(defaultSettings).get(0).description);
        assertEquals(ZonedDateTime.parse("2018-08-09T18:36:49+00:00"), channel.items(defaultSettings).get(0).pubDate);
        assertNull(channel.items(defaultSettings).get(0).guid);
    }

    @Test
    @DisplayName("RSS 1 feed with empty description")
    public void testRss1FeedWithEmptyDesc() throws Exception {
        URL url = RssFeedTest.class.getResource("/sample-rss-1-empty-desc.xml");
        RssChannel channel = new RssFeed(url).read();
        assertNull(channel.description);
    }

    @Test
    @DisplayName("RSS 1 feed with full item resource name")
    public void testRss1FeedWithFullItemResourcename() throws Exception {
        URL url = RssFeedTest.class.getResource("/sample-rss1-full-item-resource-attr-name.xml");
        new RssFeed(url).read();
    }

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

        assertEquals(4, channel.items(defaultSettings).size());
        assertEquals("Star City", channel.items(defaultSettings).get(0).title);
        assertEquals("http://liftoff.msfc.nasa.gov/news/2003/news-starcity.asp", channel.items(defaultSettings).get(0).link);
        assertEquals("How do Americans get ready to work with Russians aboard the International Space Station? "
                + "They take a crash course in culture, language and protocol at Russia's "
                + "<a href=\"http://howe.iki.rssi.ru/GCTC/gctc_e.htm\">Star City</a>.",
                channel.items(defaultSettings).get(0).description);
        assertEquals(ZonedDateTime.parse("2003-06-03T09:39:21+00:00"), channel.items(defaultSettings).get(0).pubDate);
        assertEquals("http://liftoff.msfc.nasa.gov/2003/06/03.html#item573", channel.items(defaultSettings).get(0).guid);
    }

    @Test
    @DisplayName("RSS 2 feed with apostrophe in item title")
    public void testRss2FeedWithApostropheInItemTitle() throws Exception {
        URL url = RssFeedTest.class.getResource("/sample-rss-2-apostrophe-in-title.xml");
        RssChannel channel = new RssFeed(url).read();
        assertEquals("Who’s Behind the Screencam Extortion Scam?", channel.items(defaultSettings).get(0).title);
    }

    @Test
    @DisplayName("RSS 2 feed with new line in date")
    public void testRss2FeedWithNewLineInDate() throws Exception {
        URL url = RssFeedTest.class.getResource("/sample-rss-2-with-newline-in-date.xml");
        new RssFeed(url).read();
    }

    @Test
    @DisplayName("Extract recent channel items")
    public void recentChannelItems() {
        var now = ZonedDateTime.now();
        var settings = new Settings();
        settings.lastDays = 7;

        var items = IntStream.range(0, 13).mapToObj(i -> {
            var pubDate = now.minusDays(i);
            return new RssItem("Title" + i, "Link" + i, "Desc" + i, pubDate, null);
        }).collect(Collectors.toList());
        Collections.shuffle(items);

        var channel = new RssChannel("Title", "Link", null, null, null, null, null, null, null, null, items);
        var recentItems = channel.items(settings);

        assertEquals(7, recentItems.size());
        recentItems.forEach(item -> assertTrue(item.pubDate.isAfter(now.minusDays(7))));
    }
}
