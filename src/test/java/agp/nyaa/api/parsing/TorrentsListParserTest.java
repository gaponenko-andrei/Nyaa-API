package agp.nyaa.api.parsing;

import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.test.TestTorrentsList;
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
    return TestTorrentsList.nonEmpty().getTorrentPreviewElements().first();
  }

  @Test
  public void parsingEmptyTorrentsListElementThrowsNoExceptions() {
    parse(newEmptyTorrentsListElement());
  }

  private Element newEmptyTorrentsListElement() {
    return TestTorrentsList.empty().get();
  }

  private Set<TorrentPreview> parse(final Element torrentListElement) {
    return parser.parse(torrentListElement);
  }
}
