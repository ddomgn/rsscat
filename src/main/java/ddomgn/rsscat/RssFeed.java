package ddomgn.rsscat;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
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
            if (endOf("rss", event))
                break;
            if (startOf("channel", event))
                result = parseChannel(reader);
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
            if (endOf("channel", event))
                break;
            if (startOf("title", event))
                title = nextEventData(reader, null);
            if (startOf("link", event))
                link = nextEventData(reader, null);
            if (startOf("description", event))
                description = nextEventData(reader, null);
            if (startOf("language", event))
                language = nextEventData(reader, null);
            if (startOf("pubdate", event))
                pubDate = strToZonedDateTime(nextEventData(reader, null));
            if (startOf("lastbuilddate", event))
                lastBuildDate = strToZonedDateTime(nextEventData(reader, null));
            if (startOf("docs", event))
                docs = nextEventData(reader, null);
            if (startOf("generator", event))
                generator = nextEventData(reader, null);
            if (startOf("managingeditor", event))
                managingEditor = nextEventData(reader, null);
            if (startOf("webmaster", event))
                webMaster = nextEventData(reader, null);
            if (startOf("item", event))
                items.add(parseItem(reader));
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
            if (startOf("title", event))
                title = nextEventData(reader, null);
            if (startOf("link", event))
                link = nextEventData(reader, null);
            if (startOf("description", event))
                description = nextEventData(reader, "description");
            if (startOf("pubdate", event))
                pubDate = strToZonedDateTime(nextEventData(reader, null));
            if (startOf("guid", event))
                guid = nextEventData(reader, null);
            if (endOf("item", event))
                break;
        }
        return new RssItem(title, link, description, pubDate, guid);
    }
}
