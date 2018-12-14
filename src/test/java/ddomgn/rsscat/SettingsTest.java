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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    @Test
    @DisplayName("Unknown option")
    public void unknownOption() {
        String[] args = { "-invalidOption" };
        UnsupportedOperationException error = assertThrows(UnsupportedOperationException.class,
                () -> new Settings().parseCmdOptions(args));
        assertEquals("Unknown option " + args[0], error.getMessage());
    }

    @Test
    @DisplayName("Last days option")
    public void lastDaysOption() {
        String[] args = { "-last-days", "3" };
        Settings settings = new Settings();
        assertEquals(Integer.MAX_VALUE, settings.lastDays);
        settings.parseCmdOptions(args);
        assertEquals(3, settings.lastDays);
    }

    @Test
    @DisplayName("Feeds URLs")
    public void feedsUrls() throws Exception {
        String[] args = { "http://url1.org", "-help", "http://url2.org" };
        Settings settings = new Settings();
        assertTrue(settings.feedUrls().collect(Collectors.toList()).isEmpty());

        settings.parseCmdOptions(args);
        List<URL> feedUrls = settings.feedUrls().collect(Collectors.toList());
        assertEquals(2, feedUrls.size());
        assertEquals(new URL(args[0]), feedUrls.get(0));
        assertEquals(new URL(args[2]), feedUrls.get(1));
    }

    @Test
    @DisplayName("Load feeds in parallel option")
    public void loadFeedsInParallelOption() {
        String[] args = { "-p" };
        Settings settings = new Settings();
        assertFalse(settings.loadFeedsInParallel);
        assertFalse(settings.feedUrls().isParallel());

        settings.parseCmdOptions(args);
        assertTrue(settings.loadFeedsInParallel);
        assertTrue(settings.feedUrls().isParallel());
    }

    @Test
    @DisplayName("Read feed URLs from a file")
    public void readFeedUrlsFromFile() {
        String[] args = { "-f", getClass().getResource("/feeds.txt").toString() };
        Settings settings = new Settings();
        assertTrue(settings.feedUrls().collect(Collectors.toList()).isEmpty());

        settings.parseCmdOptions(args);
        List<URL> feedUrls = settings.feedUrls().collect(Collectors.toList());
        assertEquals(4, feedUrls.size());
        IntStream.range(0, 3).forEach(i -> assertEquals("http://url" + i + ".org", feedUrls.get(i).toString()));
    }

    @Test
    @DisplayName("Hide feed description")
    public void hideFeedDescription() {
        String[] args = { "-D" };
        Settings settings = new Settings();
        assertFalse(settings.hideFeedDescription);

        settings.parseCmdOptions(args);
        assertTrue(settings.hideFeedDescription);
    }

    @Test
    @DisplayName("Determine if help should be printed")
    public void shouldPrintTest() {
        Function<String[], Settings> parseOpts = (args) -> new Settings().parseCmdOptions(args);
        Arrays.stream(new String[][] {
                new String[] { "-D", "-e", "-last-days", "1", "-p", "file:///dev/null" },
        }).map(parseOpts).map(Settings::shouldPrintHelp).forEach(Assertions::assertFalse);
        Arrays.stream(new String[][] {
                new String[] {},
                new String[] { "-h" },
                new String[] { "-help" },
                new String[] { "-f", getClass().getResource("/feeds.txt").toString(), "-h" },
                new String[] { "file:///dev/null", "-h" },
        }).map(parseOpts).map(Settings::shouldPrintHelp).forEach(Assertions::assertTrue);
    }
}