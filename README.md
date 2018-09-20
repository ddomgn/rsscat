# rsscat

RSS reader with command line interface.

## Isn't RSS Dying?

Yes, it is. But RSS/Atom feeds is a truly open standard and a way to get what _you_ want without filtering and censorship. Any social network tries to give users what they like, effectively hiding "not interesting" information. The key point is that _social networks_ decide what is interesting, not users.

And, while dying, RSS/Atom feeds have no alternatives `:-(`

## Building and Basic Usage

```Bash
cd /path/to/rsscat/
./gradlew assemble
```

The above command create a JAR file in `./build/libs/` subdirectory. Now you can use `java -jar` for reading RSS feeds:

```Bash
java -jar rsscat.jar https://static.fsf.org/fsforg/rss/news.xml
```

You can specify more than one RSS feed. Use `-help` option for more information.

## Known Issues

__rsscat__ supports RSS 1/2 feeds.

## Changelog

### 0.2.0

Features:

- Hide empty feeds by default (use `-e` to show)
- `-last-days` option support
- RSS 1 support

Bug fixes:

- Error on non-printable chars in time values
- Escaped characters in item title are nor properly processed
- Empty channel description is printed as `Optional[]`
- Full resource attribute name for RSS 1 item
- Empty RSS 2 values are not properly processed

