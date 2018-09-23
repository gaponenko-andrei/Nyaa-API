package agp.nyaa.api.parser;

import agp.nyaa.api.exception.parse.TorrentPreviewParseException;
import agp.nyaa.api.model.TorrentPreview;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import java.io.IOException;

import static agp.nyaa.api.HtmlResource.EMPTY_TORRENTS_LIST;
import static agp.nyaa.api.HtmlResource.TORRENTS_LIST;
import static org.testng.Assert.assertNotNull;

public class TorrentPreviewParserTest {

  private TorrentPreviewParser parser = new TorrentPreviewParser();

  @Test
  public void parsing() throws IOException {
    assertNotNull(parse(newValidTorrentPreviewElement()));
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void throwsOnNullElementArgument() {
    parse(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNonTorrentPreviewElement() throws IOException {
    parse(newNonTorrentPreviewElement());
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidTorrentDownloadUri() {
    parse(newElementWithInvalidDownloadUri());
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidTorrentMagnetUri() {
    parse(newElementWithInvalidMagnetUri());
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidUploadDate() {
    parse(newElementWithInvalidUploadDate());
  }

  private Element newValidTorrentPreviewElement() {
    return getTorrentPreviewListElementByIndex(0);
  }

  private Element newElementWithInvalidDownloadUri() {
    return getTorrentPreviewListElementByIndex(1);
  }

  private Element newElementWithInvalidMagnetUri() {
    return getTorrentPreviewListElementByIndex(2);
  }

  private Element newElementWithInvalidUploadDate() {
    return getTorrentPreviewListElementByIndex(3);
  }

  private Element getTorrentPreviewListElementByIndex(final int index) {
    return TORRENTS_LIST.asDocument().select("tbody tr").get(index);
  }

  private Element newNonTorrentPreviewElement() {
    return EMPTY_TORRENTS_LIST.asDocument();
  }

  private TorrentPreview parse(final Element torrentPreviewElement) {
    return parser.parse(torrentPreviewElement);
  }
}
