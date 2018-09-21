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

import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrinterTest {

    private final Settings defaultSettings = new Settings();
    private final ZonedDateTime now = ZonedDateTime.now();

    private RssChannel createChannel(ZonedDateTime itemTime) {
        var items = IntStream.range(1, 2) .mapToObj(i -> new RssItem(
                "ItemTitle" + i,
                "ItemLink" + i,
                "ItemDesc" + i,
                itemTime,
                "ItemGuid" + i)
        ).collect(Collectors.toList());
        return new RssChannel("ChTitle", "ChLink", "ChDesc",
                new RssChannel.Dates(ZonedDateTime.now(), ZonedDateTime.now()),
                new RssChannel.Details("ChLanguage", "ChDocs", "ChGenerator", "ChManagingEditor", "ChWebMaster"),
                items);
    }

    @Test
    @DisplayName("Channel printer")
    void printChannel() {
        var output = Printer.printChannel(createChannel(now), defaultSettings);
        assertEquals("ChTitle: ChDesc\n"
                        + Printer.INDENT + "ItemTitle1\n"
                        + Printer.INDENT.repeat(2) + "ItemLink1\n"
                        + Printer.INDENT.repeat(2) + now.toString() + "\n",
                output);
    }

    @Test
    @DisplayName("Hide channel description")
    void hideChannelDescription() {
        var settings = new Settings();
        settings.hideFeedDescription = true;
        var output = Printer.printChannel(createChannel(now), settings);
        assertEquals("ChTitle\n"
                        + Printer.INDENT + "ItemTitle1\n"
                        + Printer.INDENT.repeat(2) + "ItemLink1\n"
                        + Printer.INDENT.repeat(2) + now.toString() + "\n",
                output);
    }
}