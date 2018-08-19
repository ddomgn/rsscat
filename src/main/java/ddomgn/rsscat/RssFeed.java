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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

public class RssFeed {

    private final URL url;

    public RssFeed(URL url) {
        this.url = url;
    }

    public RssChannel read() throws XMLStreamException, IOException {
        InputStream inputStream = url.openStream();
        XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(inputStream);
        RssChannel result = parseRss(reader);
        reader.close();
        inputStream.close();
        return result;
    }

    private boolean nameEquals(QName name, String value) {
        return name.toString().toLowerCase().equals(value);
    }

    private boolean startOf(String tagName, XMLEvent event) {
        return event.isStartElement() && nameEquals(event.asStartElement().getName(), tagName);
    }

    private boolean endOf(String tagName, XMLEvent event) {
        return event.isEndElement() && nameEquals(event.asEndElement().getName(), tagName);
    }

    private String nextEventData(XMLEventReader reader, String untilEndTag) throws XMLStreamException {
        XMLEvent event = reader.nextEvent();
        StringBuilder data = new StringBuilder(event.asCharacters().getData());
        if (untilEndTag != null) {
            while (reader.hasNext()) {
                event = reader.nextEvent();
                if (event.isEndElement() && event.asEndElement().getName().toString().equals(untilEndTag)) {
                    break;
                }
                data.append(event.asCharacters().getData());
            }
        }
        return data.toString();
    }

    private RssChannel parseRss(XMLEventReader reader) throws XMLStreamException {
        RssChannel result = null;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (endOf("rss", event)) {
                break;
            } else if (startOf("channel", event)) {
                result = parseChannel(reader);
            }
        }
        return result;
    }

    private RssChannel parseChannel(XMLEventReader reader) throws XMLStreamException {
        String title = null, link = null, description = null, language = null, docs = null, generator = null,
                managingEditor = null, webMaster = null;
        ZonedDateTime pubDate = null, lastBuildDate = null;
        List<RssItem> items = new ArrayList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (endOf("channel", event)) {
                break;
            } else if (startOf("title", event)) {
                title = nextEventData(reader, null);
            } else if (startOf("link", event)) {
                link = nextEventData(reader, null);
            } else if (startOf("description", event)) {
                description = nextEventData(reader, null);
            } else if (startOf("language", event)) {
                language = nextEventData(reader, null);
            } else if (startOf("pubdate", event)) {
                pubDate = strToZonedDateTime(nextEventData(reader, null));
            } else if (startOf("lastbuilddate", event)) {
                lastBuildDate = strToZonedDateTime(nextEventData(reader, null));
            } else if (startOf("docs", event)) {
                docs = nextEventData(reader, null);
            } else if (startOf("generator", event)) {
                generator = nextEventData(reader, null);
            } else if (startOf("managingeditor", event)) {
                managingEditor = nextEventData(reader, null);
            } else if (startOf("webmaster", event)) {
                webMaster = nextEventData(reader, null);
            } else if (startOf("item", event)) {
                items.add(parseItem(reader));
            }
        }
        return new RssChannel(title, link, description, language, pubDate, lastBuildDate, docs, generator,
                managingEditor, webMaster, items);
    }

    private ZonedDateTime strToZonedDateTime(String str) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(DateTimeFormatter.RFC_1123_DATE_TIME)
                .toFormatter();
        return formatter.parse(str, ZonedDateTime::from);
    }

    private RssItem parseItem(XMLEventReader reader) throws XMLStreamException {
        String title = null, link = null, description = null, guid = null;
        ZonedDateTime pubDate = null;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (startOf("title", event)) {
                title = nextEventData(reader, null);
            } else if (startOf("link", event)) {
                link = nextEventData(reader, null);
            } else if (startOf("description", event)) {
                description = nextEventData(reader, "description");
            } else if (startOf("pubdate", event)) {
                pubDate = strToZonedDateTime(nextEventData(reader, null));
            } else if (startOf("guid", event)) {
                guid = nextEventData(reader, null);
            } else if (endOf("item", event)) {
                break;
            }
        }
        return new RssItem(title, link, description, pubDate, guid);
    }
}
