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
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RssFeedTest {

    @Test
    @DisplayName("RSS 1 feed test")
    public void testRss1Feed() throws Exception {
        URL url = RssFeedTest.class.getResource("/sample-rss-1.xml");
        RssFeed feed = new RssFeed(url);

        RssChannel channel = feed.read();
        assertEquals("XML.com", channel.title);
        assertEquals("http://xml.com/pub", channel.link);
        assertEquals(Optional.of("XML.com features a rich mix of information and services for the XML community."), channel.description);
        assertFalse(channel.language.isPresent());
        assertFalse(channel.pubDate.isPresent());
        assertFalse(channel.lastBuildDate.isPresent());
        assertFalse(channel.docs.isPresent());
        assertFalse(channel.generator.isPresent());
        assertFalse(channel.managingEditor.isPresent());
        assertFalse(channel.webMaster.isPresent());

        assertEquals(2, channel.items.size());
        assertEquals("Processing Inclusions with XSLT", channel.items.get(0).title);
        assertEquals("http://xml.com/pub/2000/08/09/xslt/xslt.html", channel.items.get(0).link);
        assertEquals("Processing document inclusions with general XML tools can be problematic. This article proposes "
                + "a way of preserving inclusion information through SAX-based processing.",
                channel.items.get(0).description);
        assertEquals(ZonedDateTime.parse("2018-08-09T18:36:49+00:00"), channel.items.get(0).pubDate.orElseThrow(Error::new));
        assertFalse(channel.items.get(0).guid.isPresent());
    }

    @Test
    @DisplayName("RSS 1 feed with empty description")
    public void testRss1FeedWithEmptyDesc() throws Exception {
        URL url = RssFeedTest.class.getResource("/sample-rss-1-empty-desc.xml");
        RssChannel channel = new RssFeed(url).read();
        assertFalse(channel.description.isPresent());
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
        assertEquals(Optional.of("Liftoff to Space Exploration."), channel.description);
        assertEquals("en-us", channel.language.orElseThrow(Error::new));
        assertEquals(ZonedDateTime.parse("2003-06-10T04:00:00+00:00"), channel.pubDate.orElseThrow(Error::new));
        assertEquals(ZonedDateTime.parse("2003-06-10T09:41:01+00:00"), channel.lastBuildDate.orElseThrow(Error::new));
        assertEquals("http://blogs.law.harvard.edu/tech/rss", channel.docs.orElseThrow(Error::new));
        assertEquals("Weblog Editor 2.0", channel.generator.orElseThrow(Error::new));
        assertEquals("editor@example.com", channel.managingEditor.orElseThrow(Error::new));
        assertEquals("webmaster@example.com", channel.webMaster.orElseThrow(Error::new));

        assertEquals(4, channel.items.size());
        assertEquals("Star City", channel.items.get(0).title);
        assertEquals("http://liftoff.msfc.nasa.gov/news/2003/news-starcity.asp", channel.items.get(0).link);
        assertEquals("How do Americans get ready to work with Russians aboard the International Space Station? "
                + "They take a crash course in culture, language and protocol at Russia's "
                + "<a href=\"http://howe.iki.rssi.ru/GCTC/gctc_e.htm\">Star City</a>.",
                channel.items.get(0).description);
        assertEquals(ZonedDateTime.parse("2003-06-03T09:39:21+00:00"), channel.items.get(0).pubDate.orElseThrow(Error::new));
        assertEquals("http://liftoff.msfc.nasa.gov/2003/06/03.html#item573", channel.items.get(0).guid.orElseThrow(Error::new));
    }
}
