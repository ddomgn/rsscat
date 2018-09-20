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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

class Settings {

    boolean showEmptyFeeds;
    boolean helpRequired;
    int lastDays = Integer.MAX_VALUE;
    private final List<URL> feedUrls = new ArrayList<>();
    boolean loadFeedsInParallel;

    void printHelp() {
        out.println("rsscat: RSS reader with command line interface\n" +
                "Usage:\n" +
                "    java -jar rsscat -h\n" +
                "    java -jar rsscat URL1 [URL2 [...]]\n" +
                "Options:\n" +
                "    -e\n" +
                "        Show empty feeds\n" +
                "    -f URL\n" +
                "        Read feed URLs from this URL, e.g. file:///path/to/file. Each non-empty line that does not\n" +
                "        start with # character (which is used for comments) is treated as feed URL.\n" +
                "    -h, -help\n" +
                "        Print help and exit\n" +
                "    -last-days NUM\n" +
                "        Print feed items published during NUM days\n" +
                "    -p\n" +
                "        Load feeds in parallel"
        );
    }

    Settings parseCmdOptions(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            var currentArg = args[i];
            if (currentArg.equals("-e")) {
                showEmptyFeeds = true;
            } else if (currentArg.equals("-f")) {
                feedUrls.addAll(feedUrlsFromUrl(args[++i]));
            } else if (currentArg.equals("-h") || currentArg.equals("-help")) {
                helpRequired = true;
            } else if (currentArg.equals("-last-days")) {
                lastDays = Integer.valueOf(args[++i]);
            } else if (currentArg.equals("-p")) {
                loadFeedsInParallel = true;
            } else if (currentArg.charAt(0) == '-') {
                throw new UnsupportedOperationException("Unknown option " + currentArg);
            } else {
                feedUrls.add(new URL(currentArg));
            }
        }
        return this;
    }

    private URL strToUrl(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new Error(e);
        }
    }

    private Collection<URL> feedUrlsFromUrl(String url) throws IOException {
        try (var inputStream = new URL(url).openStream()) {
            var reader = new BufferedReader(new InputStreamReader(inputStream));
            Predicate<String> lineToConvert = v -> !v.isEmpty() && !v.matches("^\\s*#.*");
            return reader.lines().filter(lineToConvert).map(this::strToUrl).collect(Collectors.toList());
        }
    }

    Stream<URL> feedUrls() { return loadFeedsInParallel ? feedUrls.stream().parallel() : feedUrls.stream(); }
}
