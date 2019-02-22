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

import java.net.URL;
import java.util.function.Function;

public class App {

    public static void main(String[] args) throws Exception {
        new App().doStuff(new Settings().parseCmdOptions(args));
    }

    private void doStuff(Settings settings) {
        if (settings.shouldPrintHelp()) {
            settings.printHelp();
        } else {
            String output = settings
                    .feedUrls()
                    .map(urlToChannel)
                    .filter(v -> v.isRight() || v.getLeft().orElseThrow().shouldBeShown(settings))
                    .map(result -> {
                        if (result.isLeft()) {
                            return result.getLeft().map(v -> Printer.printChannel(v, settings)).orElse("");
                        } else {
                            Throwable error = result.getRight().orElse(new Error("Unknown error"));
                            return error.getMessage() + ":\n" + error.getCause();
                        }
                    })
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("");
            System.out.println(output);
        }
    }

    private Function<URL, Either<RssChannel, Throwable>> urlToChannel = url -> {
        try {
            return Either.left(new RssFeed(url).read());
        } catch (Throwable e) {
            return Either.right(new Error("Failed to read " + url, e));
        }
    };
}
