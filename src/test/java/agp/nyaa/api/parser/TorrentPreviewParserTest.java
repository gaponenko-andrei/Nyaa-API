package agp.nyaa.api.parser;

import agp.nyaa.api.exception.parse.TorrentPreviewParseException;
import agp.nyaa.api.mapper.StringToUriMapper;
import agp.nyaa.api.model.Category;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentState;
import lombok.val;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static agp.nyaa.api.HtmlResource.EMPTY_TORRENTS_LIST;
import static agp.nyaa.api.HtmlResource.TORRENTS_LIST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TorrentPreviewParserTest {

  private TorrentPreviewParser parser = new TorrentPreviewParser();


  @Test(expectedExceptions = NullPointerException.class)
  public void throwsOnNullElementArgument() {
    parse(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNonTorrentPreviewElement() throws IOException {
    parse(newNonTorrentPreviewElement());
  }

  private Element newNonTorrentPreviewElement() {
    return EMPTY_TORRENTS_LIST.asDocument();
  }

  @Test
  public void parsingReturnsNonNullResult() throws IOException {
    assertNotNull(parse(newValidTorrentPreviewElement()));
  }

  private Element newValidTorrentPreviewElement() {
    return getTorrentPreviewListElementByTestCaseId("valid-preview-element");
  }

  /* id parsing */

  @Test
  public void idParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.id().longValue(), 1032497L);
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentId() {
    parse(newElementWithAbsentId());
  }

  private Element newElementWithAbsentId() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-id");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidId() {
    parse(newElementWithInvalidId());
  }

  private Element newElementWithInvalidId() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-id");
  }

  /* state parsing */

  @Test
  public void stateParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.state(), TorrentState.NORMAL);
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentState() {
    parse(newElementWithAbsentState());
  }

  private Element newElementWithAbsentState() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-state");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithUnknownState() {
    parse(newElementWithUnknownState());
  }

  private Element newElementWithUnknownState() {
    return getTorrentPreviewListElementByTestCaseId("with-unknown-state");
  }

  /* category parsing */

  @Test
  public void categoryParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.category(), Category.Anime.EnglishTranslated.instance());
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentCategory() {
    parse(newElementWithAbsentCategory());
  }

  private Element newElementWithAbsentCategory() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-category");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithUnknownCategory() {
    parse(newElementWithUnknownCategory());
  }

  private Element newElementWithUnknownCategory() {
    return getTorrentPreviewListElementByTestCaseId("with-unknown-category");
  }

  /* title parsing */

  @Test
  public void titleParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.title(), "[Erai-raws] Tokyo Ghoul-re - 05 [720p].mkv");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentTitle() {
    parse(newElementWithAbsentTitle());
  }

  private Element newElementWithAbsentTitle() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-title");
  }

  /* torrent download uri parsing */

  @Test
  public void torrentDownloadUriParsing() {
    val result = parse(newValidTorrentPreviewElement());
    val expectedResult = StringToUriMapper.applicationTo("/download/1032497.torrent");
    assertEquals(result.downloadLink(), expectedResult);
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentTorrentDownloadUri() {
    parse(newElementWithAbsentTorrentDownloadUri());
  }

  private Element newElementWithAbsentTorrentDownloadUri() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-torrent-download-uri");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidTorrentDownloadUri() {
    parse(newElementWithInvalidTorrentDownloadUri());
  }

  private Element newElementWithInvalidTorrentDownloadUri() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-torrent-download-uri");
  }

  /* magnet uri parsing */

  @Test
  public void magnetUriParsing() {
    val result = parse(newValidTorrentPreviewElement());
    val expectedResult = StringToUriMapper.applicationTo(
      "magnet:?xt=urn:btih:BGS7JJ4XMFBB36BRTQVDJ75E5BMUJISR&dn=%5BErai-raws%5D+Tokyo+Ghoul-re+-+05" +
        "+%5B720p%5D.mkv&tr=http%3A%2F%2Fnyaa.tracker.wf%3A7777%2Fannounce&tr=udp%3A%2F%2Fopen." +
        "stealth.si%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=" +
        "udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-" +
        "paradise.org%3A6969%2Fannounce");

    assertEquals(result.magnetLink(), expectedResult);
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentMagnetUri() {
    parse(newElementWithAbsentMagnetUri());
  }

  private Element newElementWithAbsentMagnetUri() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-magnet-uri");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidMagnetUri() {
    parse(newElementWithInvalidMagnetUri());
  }

  private Element newElementWithInvalidMagnetUri() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-magnet-uri");
  }

  /* upload date parsing */

  @Test
  public void uploadDateParsing() {
    val result = parse(newValidTorrentPreviewElement());
    val expectedResult = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse("2018-05-01 16:10");
    assertEquals(result.uploadDate(), expectedResult);
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentUploadDate() {
    parse(newElementWithAbsentUploadDate());
  }

  private Element newElementWithAbsentUploadDate() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-upload-date");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidUploadDate() {
    parse(newElementWithInvalidUploadDate());
  }

  private Element newElementWithInvalidUploadDate() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-upload-date");
  }

  /* utility methods */

  private Element getTorrentPreviewListElementByTestCaseId(final String testCaseId) {
    val selector = String.format("tbody tr[data-test-case-id='%s']", testCaseId);
    return TORRENTS_LIST.asDocument().select(selector).first();
  }

  private TorrentPreview parse(final Element torrentPreviewElement) {
    return parser.parse(torrentPreviewElement);
  }
}
