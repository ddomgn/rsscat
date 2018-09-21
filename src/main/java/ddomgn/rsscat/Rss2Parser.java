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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

class Rss2Parser extends XmlParser implements Parser {

    @Override
    public RssChannel parse(XMLEventReader reader) throws ParseException {
        RssChannel result = null;
        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (isEndTag("rss", event)) {
                    break;
                } else if (isStartTag("channel", event)) {
                    result = parseChannel(reader);
                    break;
                }
            }
        } catch (XMLStreamException e) {
            throw new ParseException(e);
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
            if (isEndTag("channel", event)) {
                break;
            } else if (isStartTag("title", event)) {
                title = nextEventData(reader, null);
            } else if (isStartTag("link", event)) {
                link = nextEventData(reader, null);
            } else if (isStartTag("description", event)) {
                description = nextEventData(reader, null);
            } else if (isStartTag("language", event)) {
                language = nextEventData(reader, null);
            } else if (isStartTag("pubdate", event)) {
                pubDate = strToZonedDateTime(nextEventData(reader, null));
            } else if (isStartTag("lastbuilddate", event)) {
                lastBuildDate = strToZonedDateTime(nextEventData(reader, null));
            } else if (isStartTag("docs", event)) {
                docs = nextEventData(reader, null);
            } else if (isStartTag("generator", event)) {
                generator = nextEventData(reader, null);
            } else if (isStartTag("managingeditor", event)) {
                managingEditor = nextEventData(reader, null);
            } else if (isStartTag("webmaster", event)) {
                webMaster = nextEventData(reader, null);
            } else if (isStartTag("item", event)) {
                items.add(parseItem(reader));
            }
        }
        return new RssChannel(title, link, description,
                new RssChannel.Dates(pubDate, lastBuildDate),
                new RssChannel.Details(language, docs, generator, managingEditor, webMaster),
                items);
    }

    private RssItem parseItem(XMLEventReader reader) throws XMLStreamException {
        String title = null, link = null, description = null;
        String guid = null;
        ZonedDateTime pubDate = null;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (isStartTag("title", event)) {
                title = nextEventData(reader, "title");
            } else if (isStartTag("link", event)) {
                link = nextEventData(reader, null);
            } else if (isStartTag("description", event)) {
                description = nextEventData(reader, "description");
            } else if (isStartTag("pubdate", event)) {
                pubDate = strToZonedDateTime(nextEventData(reader, null));
            } else if (isStartTag("guid", event)) {
                guid = nextEventData(reader, null);
            } else if (isEndTag("item", event)) {
                break;
            }
        }
        return new RssItem(title, link, description, pubDate, guid);
    }
}
