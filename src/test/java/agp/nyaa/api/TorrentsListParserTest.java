package agp.nyaa.api;

import agp.nyaa.api.parser.Parser;
import agp.nyaa.api.parser.TorrentListElementParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import properties.Constants;

import java.io.IOException;

import static agp.nyaa.api.HtmlTestResource.EMPTY_TORRENT_LIST;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TorrentsListParserTest {

  private static final String TORRENT_LIST_SELECTOR = "." + Constants.torrent.list.className;

  @Test
  void parse() throws IOException {

    /* Arrange */
    final Element resourceBody = EMPTY_TORRENT_LIST.asDocumentBody();
    final Element torrentList = resourceBody.selectFirst(TORRENT_LIST_SELECTOR);

    /* Act */
    new TorrentListElementParser().parse(torrentList);
  }

  @Test
  void parseThrowsWhenPageIsNotTorrentList() throws IOException {

    /* Arrange */
    final Document document = EMPTY_TORRENT_LIST.asDocument();
    final Parser parser = new TorrentListElementParser();

    /* Act & Assert */
    assertThrows(IllegalArgumentException.class, () -> parser.parse(document));
  }
}
