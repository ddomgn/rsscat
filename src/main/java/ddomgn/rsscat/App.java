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

public class App {

    public static void main(String[] args) {
        Settings settings = new Settings();
        settings.parseCmdOptions(args);
        new App().doStuff(settings);
    }

    private void doStuff(Settings settings) {
        if (settings.helpRequired) {
            settings.printHelp();
        } else {
            var output = settings.feedUrls.stream().map(url -> {
                try {
                    return new RssFeed(url).read();
                } catch (Exception e) {
                    throw new Error(e);
                }
            }).filter(ch -> ch.shouldBeShown(settings)).map(ch -> Printer.printChannel(ch, settings)).reduce((a, b) -> a + "\n" + b).orElse("");
            System.out.println(output);
        }
    }
}
