# rsscat

RSS reader with command line interface.

## Building and Basic Usage

```Bash
cd /path/to/rsscat/
./gradlew assemble
```

The above command create a JAR file in `./build/libs/` subdirectory. Now you can use `java -jar` for reading RSS feeds:

```Bash
java -jar rsscat.jar https://static.fsf.org/fsforg/rss/news.xml
```

You can specify more than one RSS feed.

## Known Issues

Currently __rsscat__ supports only RSS 2.0 feeds. The future version will additionally support RSS 1.0.

