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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class RssFeed {

    private final URL url;

    RssFeed(URL url) { this.url = url; }

    RssChannel read() throws ParseException, IOException {
        RssChannel result;
        try (InputStream inputStream = url.openStream()) {
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(inputStream);
            result = new RootParser().parse(reader);
            reader.close();
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return result;
    }
}
