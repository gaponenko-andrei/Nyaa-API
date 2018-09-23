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
  private Element torrentPreviewElement;
  private TorrentPreview parsingResult;


  @Test
  public void parsing() throws IOException {

    /* Arrange */
    givenValidTorrentPreviewElement();

    /* Act */
    parseElement();

    /* Assert */
    assertNotNull(parsingResult);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNonTorrentPreviewElement() throws IOException {

    /* Arrange */
    givenNonTorrentPreviewElement();

    /* Act */
    parseElement();
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void throwsOnNullElementArgument() {

    /* Arrange */
    givenNullTorrentPreviewElement();

    /* Act */
    parseElement();
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidTorrentDownloadUri() throws IOException {

    /* Arrange */
    givenElementWithInvalidDownloadUri();

    /* Act */
    parseElement();
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidTorrentMagnetUri() throws IOException {

    /* Arrange */
    givenElementWithInvalidMagnetUri();

    /* Act */
    parseElement();
  }

  private void givenValidTorrentPreviewElement() {
    torrentPreviewElement = TORRENTS_LIST.asDocument().select("tbody tr").get(0);
  }

  private void givenElementWithInvalidDownloadUri() {
    torrentPreviewElement = TORRENTS_LIST.asDocument().select("tbody tr").get(1);
  }

  private void givenElementWithInvalidMagnetUri() {
    torrentPreviewElement = TORRENTS_LIST.asDocument().select("tbody tr").get(2);
  }

  private void givenElementWithInvalidUploadDate() {
    torrentPreviewElement = TORRENTS_LIST.asDocument().select("tbody tr").get(3);
  }

  private Element getTorrentPreviewListElementByIndex(final int index) {
    return TORRENTS_LIST.asDocument().select("tbody tr").get(index);
  }

  private void givenNonTorrentPreviewElement() {
    torrentPreviewElement = EMPTY_TORRENTS_LIST.asDocument();
  }

  private void givenNullTorrentPreviewElement() {
    torrentPreviewElement = null;
  }

  private void parseElement() {
    parsingResult = parser.parse(torrentPreviewElement);
  }
}
