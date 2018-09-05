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

class RootParser extends XmlParser implements Parser {

    @Override
    public RssChannel parse(XMLEventReader reader) throws ParseException {
        Parser specificParser = null;
        try {
            while (reader.hasNext() && specificParser == null) {
                XMLEvent event = reader.nextEvent();
                if (isStartTag("{http://www.w3.org/1999/02/22-rdf-syntax-ns#}RDF", event)) {
                    specificParser = new Rss1Parser();
                } else if (isStartTag("rss", event)) {
                    specificParser = new Rss2Parser();
                }
            }
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return (specificParser == null) ? null : specificParser.parse(reader);
    }
}
