package agp.nyaa.api.parsing;

import agp.nyaa.api.exception.parse.TorrentPreviewParseException;
import agp.nyaa.api.mapper.StringToUriMapper;
import agp.nyaa.api.model.Category;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentState;
import com.google.common.primitives.UnsignedInteger;
import lombok.val;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
  public void uploadInstantParsing() {

    /* Arrange */
    val expectedDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    val expectedLocalDateTime = LocalDateTime.parse("2018-05-01 16:10", expectedDateFormatter);
    val expectedInstant = expectedLocalDateTime.toInstant(ZoneOffset.UTC);

    /* Act */
    val result = parse(newValidTorrentPreviewElement());

    /* Assert */
    assertEquals(result.uploadInstant(), expectedInstant);
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentUploadInstant() {
    parse(newElementWithAbsentUploadInstant());
  }

  private Element newElementWithAbsentUploadInstant() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-upload-instant");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidUploadInstant() {
    parse(newElementWithInvalidUploadInstant());
  }

  private Element newElementWithInvalidUploadInstant() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-upload-instant");
  }

  /* seeders count parsing */

  @Test
  public void seedersCountParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.seedersCount(), UnsignedInteger.valueOf(1));
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentSeedersCount() {
    parse(newElementWithAbsentSeedersCount());
  }

  private Element newElementWithAbsentSeedersCount() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-seeders-count");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidSeedersCount() {
    parse(newElementWithInvalidSeedersCount());
  }

  private Element newElementWithInvalidSeedersCount() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-seeders-count");
  }

  /* leechers count parsing */

  @Test
  public void leechersCountParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.leechersCount(), UnsignedInteger.valueOf(39));
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentLeechersCount() {
    parse(newElementWithAbsentLeechersCount());
  }

  private Element newElementWithAbsentLeechersCount() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-leechers-count");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidLeechersCount() {
    parse(newElementWithInvalidLeechersCount());
  }

  private Element newElementWithInvalidLeechersCount() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-leechers-count");
  }

  /* downloads count parsing */

  @Test
  public void downloadsCountParsing() {
    val result = parse(newValidTorrentPreviewElement());
    assertEquals(result.downloadsCount(), UnsignedInteger.valueOf(0));
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithAbsentDownloadsCount() {
    parse(newElementWithAbsentDownloadsCount());
  }

  private Element newElementWithAbsentDownloadsCount() {
    return getTorrentPreviewListElementByTestCaseId("with-absent-downloads-count");
  }

  @Test(expectedExceptions = TorrentPreviewParseException.class)
  public void throwsOnElementWithInvalidDownloadsCount() {
    parse(newElementWithInvalidDownloadsCount());
  }

  private Element newElementWithInvalidDownloadsCount() {
    return getTorrentPreviewListElementByTestCaseId("with-invalid-downloads-count");
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
