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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static ddomgn.rsscat.Printer.printLine;

class Settings {

    boolean helpRequired;
    int lastDays = Integer.MAX_VALUE;
    final List<URL> feedUrls = new ArrayList<>();

    void printHelp() {
        printLine(0, "rsscat: RSS reader with command line interface");
        printLine(0, "Usage:");
        printLine(1, "java -jar rsscat -h");
        printLine(1, "java -jar rsscat URL1 [URL2 [...]]");
        printLine(0, "-h, -help");
        printLine(1, "Print help and exit");
        printLine(0, "-last-days NUM");
        printLine(1, "Print feed items published during NUM days");
    }

    void parseCmdOptions(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                case "-help":
                    helpRequired = true;
                    break;
                case "-last-days":
                    lastDays = Integer.valueOf(args[++i]);
                    break;
                default:
                    if (args[i].charAt(0) == '-') {
                        throw new UnsupportedOperationException("Unknown option " + args[i]);
                    } else {
                        try {
                            feedUrls.add(new URL(args[i]));
                        } catch (MalformedURLException e) {
                            throw new Error(e);
                        }
                    }
            }
        }
    }
}
