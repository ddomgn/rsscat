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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrinterTest {

    private final Settings defaultSettings = new Settings();

    @Test
    @DisplayName("Channel printer")
    void printChannel() {
        var time = ZonedDateTime.now();
        var items = IntStream.range(1, 2) .mapToObj(i -> new RssItem(
                "ItemTitle" + i,
                "ItemLink" + i,
                "ItemDesc" + i,
                Optional.of(time),
                Optional.of("ItemGuid" + i))
        ).collect(Collectors.toList());
        var channel = new RssChannel("ChTitle", "ChLink", Optional.of("ChDesc"), Optional.of("ChLanguage"),
                Optional.of(ZonedDateTime.now()), Optional.of(ZonedDateTime.now()), Optional.of("ChDocs"),
                Optional.of("ChGenerator"), Optional.of("ChManagingEditor"), Optional.of("ChWebMaster"), items);
        var output = Printer.printChannel(channel, defaultSettings);
        assertEquals("ChTitle: ChDesc\n"
                + Printer.INDENT + "ItemTitle1\n"
                + Printer.INDENT.repeat(2) + "ItemLink1\n"
                + Printer.INDENT.repeat(2) + time.toString() + "\n",
                output);
    }
}