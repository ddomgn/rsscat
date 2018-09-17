/*
 * Copyright (C) 2018 Dmitry Davletbaev <ddomgn@gmail.com>
 *
 * This file is part of rsscat.
 *
 * rsscat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *                } else if (isStartTag(fullTagName("item"), event)) {
                    items.add(parseItem(reader));
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
import javax.xml.stream.events.XMLEvent;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

abstract class XmlParser {

    boolean isStartTag(String tagName, XMLEvent event) {
        return event.isStartElement() && nameEquals(event.asStartElement().getName(), tagName);
    }

    boolean isEndTag(String tagName, XMLEvent event) {
        return event.isEndElement() && nameEquals(event.asEndElement().getName(), tagName);
    }

    String nextEventData(XMLEventReader reader, String untilEndTag) throws XMLStreamException {
        XMLEvent event = reader.nextEvent();
        StringBuilder data = new StringBuilder(event.isCharacters() ? event.asCharacters().getData() : "");
        if (untilEndTag != null) {
            while (reader.hasNext()) {
                event = reader.nextEvent();
                if (isEndTag(untilEndTag, event)) {
                    break;
                } else if (event.isCharacters()) {
                    data.append(event.asCharacters().getData());
                }
            }
        }
        return (data.length() > 0) ? data.toString() : null;
    }

    ZonedDateTime strToZonedDateTime(String str) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.RFC_1123_DATE_TIME)
                .appendOptional(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                .toFormatter();
        return formatter.parse(str.replaceAll("[^\\p{Print}]", "").trim(), ZonedDateTime::from);
    }

    private boolean nameEquals(QName name, String value) {
        return 0 == name.toString().toLowerCase().compareToIgnoreCase(value);
    }
}
