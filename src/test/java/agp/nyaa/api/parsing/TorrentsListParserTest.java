package agp.nyaa.api.parsing;

import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.test.TestTorrentsList;
import lombok.val;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Set;

public class TorrentsListParserTest {

  private TorrentsListParser parser = new TorrentsListParser();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNullElementArgument() {
    parse(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNonTorrentsListElement() throws IOException {
    parse(newNonTorrentsListElement());
  }

  private Element newNonTorrentsListElement() {
    val torrentsList = TestTorrentsList.fromResource("torrents-list-parser-test.html");
    return torrentsList.getTorrentPreviewElements().first();
  }

  @Test
  public void emptyTorrentsListParsingThrowsNoExceptions() {
    parse(newEmptyTorrentsListElement());
  }

  private Element newEmptyTorrentsListElement() {
    return TestTorrentsList.empty().get();
  }

  // todo finish
//  @Test
//  public void nonEmptyTorrentsListParsing() {
//
//    /* Arrange */
//    val nonEmptyTorrentsList = TestTorrentsList.fromResource("torrent-preview-parser-test.html").get();
//
//    /* Act */
//    val result = parse(nonEmptyTorrentsList);
//
//    /* Assert */
//
//  }

  private Set<TorrentPreview> parse(final Element torrentListElement) {
    return parser.parse(torrentListElement);
  }
}
