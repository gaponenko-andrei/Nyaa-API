package agp.nyaa.api.parser;

import agp.nyaa.api.exception.parse.TorrentPreviewParseException;
import agp.nyaa.api.model.Category;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentState;
import lombok.val;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import java.io.IOException;

import static agp.nyaa.api.HtmlResource.EMPTY_TORRENTS_LIST;
import static agp.nyaa.api.HtmlResource.TORRENTS_LIST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

// todo добавить тесты для случаев, когда атрибуты отсутствуют

public class TorrentPreviewParserTest {

  private TorrentPreviewParser parser = new TorrentPreviewParser();

  @Test
  public void parsingReturnsNonNullResult() throws IOException {
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

  /* id parsing */

  @Test
  public void idParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.id().longValue(), 1032497L);
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidId() {
    parse(newElementWithInvalidId());
  }

  /* state parsing */

  @Test
  public void stateParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.state(), TorrentState.NORMAL);
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidState() {
    parse(newElementWithInvalidState());
  }

  /* category parsing */

  @Test
  public void categoryParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.category(), Category.Anime.EnglishTranslated.instance());
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidCategory() {
    parse(newElementWithInvalidCategory());
  }

  /* title parsing */

  @Test
  public void titleParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.title(), "[Erai-raws] Tokyo Ghoul-re - 05 [720p].mkv");
  }

  // todo тесты на отсутствие атрибутов
//  @Test(expectedExceptions = TorrentPreviewParseException.class)
//  public void throwsOnElementWithAbsentTitle() {
//    parse(newElementWithAbsentTitle());
//  }

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
    return getTorrentPreviewListElementByTestCaseId("valid-preview-element");
  }

  private Element newElementWithInvalidId() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-id");
  }

  private Element newElementWithInvalidState() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-state");
  }

  private Element newElementWithInvalidCategory() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-category");
  }

  private Element newElementWithAbsentTitle() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-title");
  }

  private Element newElementWithInvalidDownloadUri() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-torrent-link");
  }

  private Element newElementWithInvalidMagnetUri() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-magnet-link");
  }

  private Element newElementWithInvalidUploadDate() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-upload-date");
  }

  private Element getTorrentPreviewListElementByTestCaseId(final String testCaseId) {
    val selector = String.format("tbody tr[data-test-case-id='%s']", testCaseId);
    return TORRENTS_LIST.asDocument().select(selector).first();
  }

  private Element newNonTorrentPreviewElement() {
    return EMPTY_TORRENTS_LIST.asDocument();
  }

  private TorrentPreview parse(final Element torrentPreviewElement) {
    return parser.parse(torrentPreviewElement);
  }
}
