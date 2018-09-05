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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.util.*;
import java.util.stream.Collectors;

public class Rss1Parser extends XmlParser implements Parser {

    @Override
    public RssChannel parse(XMLEventReader reader) throws ParseException {
        ChannelParseResult channelParseResult = null;
        List<RssItem> items = new ArrayList<>();
        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (isEndTag(inRdfNs("RDF"), event)) {
                    break;
                } else if (isStartTag(inDefaultNs("channel"), event)) {
                    channelParseResult = parseChannel(reader);
                } else if (isStartTag(inDefaultNs("item"), event)) {
                    items.add(parseItem(reader));
                }
            }
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        if (channelParseResult == null) {
            throw new ParseException("No <channel> tag found");
        } else {
            return new RssChannel(channelParseResult.title, channelParseResult.link,
                    Optional.ofNullable(channelParseResult.description),
                    Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), items);
        }
    }

    private class ChannelParseResult {
        String title;
        String link;
        String description;
        Collection<String> itemResources;
    }

    private String inRdfNs(String tagName) { return "{http://www.w3.org/1999/02/22-rdf-syntax-ns#}" + tagName; }

    private String inDefaultNs(String tagName) { return "{http://purl.org/rss/1.0/}" + tagName; }

    private ChannelParseResult parseChannel(XMLEventReader reader) throws XMLStreamException {
        ChannelParseResult result = new ChannelParseResult();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (isEndTag(inDefaultNs("channel"), event)) {
                break;
            } else if (isStartTag(inDefaultNs("title"), event)) {
                result.title = nextEventData(reader, null);
            } else if (isStartTag(inDefaultNs("link"), event)) {
                result.link = nextEventData(reader, null);
            } else if (isStartTag(inDefaultNs("description"), event)) {
                result.description = nextEventData(reader, null);
            } else if (isStartTag(inDefaultNs("items"), event)) {
                result.itemResources= parseItemResources(reader);
            }
        }
        return result;
    }

    private Collection<String> parseItemResources(XMLEventReader reader) throws XMLStreamException {
        Collection<String> result = new ArrayList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (isEndTag(inDefaultNs("items"), event)) {
                break;
            } else if (isStartTag(inRdfNs("li"), event)) {
                List<String> resourceAttrValues = Arrays.stream(new String[]{"resource", inRdfNs("resource")})
                        .map(attrName -> event.asStartElement().getAttributeByName(QName.valueOf(attrName)))
                        .filter(Objects::nonNull)
                        .map(Attribute::getValue)
                        .collect(Collectors.toList());
                result.addAll(resourceAttrValues);
            }
        }
        return result;
    }

    private RssItem parseItem(XMLEventReader reader) throws XMLStreamException {
        String title = null, link = null, description = null;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (isStartTag(inDefaultNs("title"), event)) {
                title = nextEventData(reader, null);
            } else if (isStartTag(inDefaultNs("link"), event)) {
                link = nextEventData(reader, null);
            } else if (isStartTag(inDefaultNs("description"), event)) {
                description = nextEventData(reader, inDefaultNs("description"));
            } else if (isEndTag(inDefaultNs("item"), event)) {
                break;
            }
        }
        return new RssItem(title, link, description, Optional.empty(), Optional.empty());
    }
}
