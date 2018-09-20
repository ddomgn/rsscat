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
import static java.lang.System.out;

class Settings {

    boolean showEmptyFeeds;
    boolean helpRequired;
    int lastDays = Integer.MAX_VALUE;
    final List<URL> feedUrls = new ArrayList<>();

    void printHelp() {
        out.println("rsscat: RSS reader with command line interface");
        out.println("Usage:");
        out.println("    java -jar rsscat -h");
        out.println("    java -jar rsscat URL1 [URL2 [...]]");
        out.println("Options:");
        out.println("    -e");
        out.println("        Show empty feeds");
        out.println("    -h, -help");
        out.println("        Print help and exit");
        out.println("    -last-days NUM");
        out.println("        Print feed items published during NUM days");
    }

    void parseCmdOptions(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-e":
                    showEmptyFeeds = true;
                    break;
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
