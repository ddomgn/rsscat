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

import static java.lang.System.out;

public class App {

    public static void main(String[] args) {
        Settings settings = new Settings();
        settings.parseCmdOptions(args);
        new App().doStuff(settings);
    }

    private void doStuff(Settings settings) {
        if (settings.helpRequired()) {
            printHelp();
            return;
        }
        settings.feedUrls().stream().map(url -> {
            try {
                return new RssFeed(url).read();
            } catch (Exception e) {
                throw new Error(e);
            }
        }).forEach(channel -> {
            out.println();
            printLine(0, channel.title + ": " + channel.description);
            channel.items.forEach(item -> {
                printLine(1, item.title);
                printLine(2, item.pubDate.toLocalDateTime().toString());
                printLine(2, item.link);
            });
        });
    }

    private void printHelp() {
        printLine(0, "rsscat: RSS reader with command line interface");
        printLine(0, "Usage:");
        printLine(1, "java -jar rsscat -h");
        printLine(1, "java -jar rsscat URL1 [URL2 [...]]");
        printLine(0, "-h, -help");
        printLine(1, "Print help and exit");
    }

    private void printLine(int indent, String str) {
        for (int i = 0; i < indent; i++) out.print("    ");
        out.println(str);
    }
}
