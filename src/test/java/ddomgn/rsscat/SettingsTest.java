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

import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    @Test
    @DisplayName("Unknown option")
    public void testUnknownOption() {
        String[] args = { "-invalidOption" };
        UnsupportedOperationException error = assertThrows(UnsupportedOperationException.class,
                () -> new Settings().parseCmdOptions(args));
        assertEquals("Unknown option " + args[0], error.getMessage());
    }

    @Test
    @DisplayName("Short help option")
    public void testShortHelpOption() {
        String[] args = { "-h" };
        Settings settings = new Settings();
        assertFalse(settings.helpRequired());
        settings.parseCmdOptions(args);
        assertTrue(settings.helpRequired());
    }

    @Test
    @DisplayName("Long help option")
    public void testLongHelpOption() {
        String[] args = { "-help" };
        Settings settings = new Settings();
        assertFalse(settings.helpRequired());
        settings.parseCmdOptions(args);
        assertTrue(settings.helpRequired());
    }

    @Test
    @DisplayName("Feeds URLs")
    public void testFeedsUrls() throws Exception {
        String[] args = { "http://url1.org", "-help", "http://url2.org" };
        Settings settings = new Settings();
        assertTrue(settings.feedUrls().isEmpty());

        settings.parseCmdOptions(args);
        List<URL> feedUrls = settings.feedUrls();
        assertEquals(2, feedUrls.size());
        assertEquals(new URL(args[0]), feedUrls.get(0));
        assertEquals(new URL(args[2]), feedUrls.get(1));
    }
}