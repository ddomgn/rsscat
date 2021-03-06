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

    public static void main(String[] args) throws Exception {
        new App().doStuff(new Settings().parseCmdOptions(args));
    }

    private void doStuff(Settings settings) {
        if (settings.helpRequired) {
            settings.printHelp();
        } else {
            System.out.println(settings
                    .feedUrls()
                    .map(url -> new RssFeed(url).read())
                    .filter(channel -> channel.shouldBeShown(settings))
                    .map(channel -> Printer.printChannel(channel, settings))
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("")
            );
        }
    }
}
