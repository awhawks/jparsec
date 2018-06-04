package jparsec.vo;

import java.net.URL;

public class FeedTest {
    /**
     * Test program.
     *
     * @param args Not used.
     * @throws Exception If an error occurs.
     */
    public static void main(String[] args) throws Exception {
        String file = "file:///home/alonso/2012_spanish.rss";
        //String file = "http://www.vogella.de/article.rss";
        //String file = "http://cyber.law.harvard.edu/rss/examples/rss2sample.xml";
        Feed feed = Feed.readFeed(new URL(file));
        System.out.println(feed);

        for (FeedMessageElement message : feed.getMessages()) {
            System.out.println(message);
        }

        if (file.startsWith("http")) {
            feed.getMessages().add(
                    Feed.createMessage("hellow", "greetings <a href=\"hi!\"></a>", "my link", "myself", "id", new String[] {
                            "http://www.webmasterworld.com/theme/default/gfx/rss.gif" }, "today")
            );

            feed.writeFeed("/home/alonso/test.rss");
        }
    }
}
