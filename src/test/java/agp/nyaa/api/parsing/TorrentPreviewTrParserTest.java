package agp.nyaa.api.parsing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.Test;

import com.google.common.primitives.UnsignedInteger;

import agp.nyaa.api.element.ImmutableDocument;
import agp.nyaa.api.element.Tr;
import agp.nyaa.api.mapper.StringToUriMapper;
import agp.nyaa.api.model.Category;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentState;
import agp.nyaa.api.test.TestDocuments;
import lombok.val;

// todo refactoring

public class TorrentPreviewTrParserTest {

  private TorrentPreviewTrParser parser = new TorrentPreviewTrParser();
  private ImmutableDocument trSamplesDoc = TestDocuments.get("torrent-preview-tr-samples.html");
  private Tr validTorrentPreviewTr = newTorrentPreviewTrBySampleId("valid");


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNullElementArgument() {
    parse(null);
  }

  @Test
  public void parsingReturnsNonNullResult() throws IOException {
    assertNotNull(parse(validTorrentPreviewTr));
  }

  /* id parsing */

  @Test
  public void idParsing() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.id().longValue(), 1032497L);
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentId() {
    parse(newTorrentPreviewTrBySampleId("with-absent-id"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithInvalidId() {
    parse(newTorrentPreviewTrBySampleId("with-invalid-id"));
  }

  /* state parsing */

  @Test
  public void stateParsing() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.state(), TorrentState.NORMAL);
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentState() {
    parse(newTorrentPreviewTrBySampleId("with-absent-state"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithUnknownState() {
    parse(newTorrentPreviewTrBySampleId("with-unknown-state"));
  }

  /* category parsing */

  @Test
  public void categoryParsing() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.category(), Category.Anime.EnglishTranslated.instance());
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentCategory() {
    parse(newTorrentPreviewTrBySampleId("with-absent-category"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithUnknownCategory() {
    parse(newTorrentPreviewTrBySampleId("with-unknown-category"));
  }

  /* title parsing */

  @Test
  public void titleParsing() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.title(), "[Erai-raws] Tokyo Ghoul-re - 05 [720p].mkv");
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentTitle() {
    parse(newTorrentPreviewTrBySampleId("with-absent-title"));
  }

  /* torrent download uri parsing */

  @Test
  public void torrentDownloadUriParsing() {
    val result = parse(validTorrentPreviewTr);
    val expectedResult = StringToUriMapper.applicationTo("/download/1032497.torrent");
    assertEquals(result.downloadLink(), expectedResult);
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentTorrentDownloadUri() {
    parse(newTorrentPreviewTrBySampleId("with-absent-torrent-download-uri"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithInvalidTorrentDownloadUri() {
    parse(newTorrentPreviewTrBySampleId("with-invalid-torrent-download-uri"));
  }

  /* magnet uri parsing */

  @Test
  public void magnetUriParsing() {
    val result = parse(validTorrentPreviewTr);
    val expectedResult = StringToUriMapper.applicationTo(
      "magnet:?xt=urn:btih:BGS7JJ4XMFBB36BRTQVDJ75E5BMUJISR&dn=%5BErai-raws%5D+Tokyo+Ghoul-re+-+05" +
        "+%5B720p%5D.mkv&tr=http%3A%2F%2Fnyaa.tracker.wf%3A7777%2Fannounce&tr=udp%3A%2F%2Fopen." +
        "stealth.si%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=" +
        "udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-" +
        "paradise.org%3A6969%2Fannounce");

    assertEquals(result.magnetLink(), expectedResult);
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentMagnetUri() {
    parse(newTorrentPreviewTrBySampleId("with-absent-magnet-uri"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithInvalidMagnetUri() {
    parse(newTorrentPreviewTrBySampleId("with-invalid-magnet-uri"));
  }

  /* upload date parsing */

  @Test
  public void uploadInstantParsing() {

    /* Arrange */
    val expectedDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    val expectedLocalDateTime = LocalDateTime.parse("2018-05-01 16:10", expectedDateFormatter);
    val expectedInstant = expectedLocalDateTime.toInstant(ZoneOffset.UTC);

    /* Act */
    val result = parse(validTorrentPreviewTr);

    /* Assert */
    assertEquals(result.uploadInstant(), expectedInstant);
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentUploadInstant() {
    parse(newTorrentPreviewTrBySampleId("with-absent-upload-instant"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithInvalidUploadInstant() {
    parse(newTorrentPreviewTrBySampleId("with-invalid-upload-instant"));
  }

  /* seeders count parsing */

  @Test
  public void seedersCountParsing() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.seedersCount(), UnsignedInteger.valueOf(1));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentSeedersCount() {
    parse(newTorrentPreviewTrBySampleId("with-absent-seeders-count"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithInvalidSeedersCount() {
    parse(newTorrentPreviewTrBySampleId("with-invalid-seeders-count"));
  }

  /* leechers count parsing */

  @Test
  public void leechersCountParsing() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.leechersCount(), UnsignedInteger.valueOf(39));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentLeechersCount() {
    parse(newTorrentPreviewTrBySampleId("with-absent-leechers-count"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithInvalidLeechersCount() {
    parse(newTorrentPreviewTrBySampleId("with-invalid-leechers-count"));
  }

  /* downloads count parsing */

  @Test
  public void downloadsCountParsing() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.downloadsCount(), UnsignedInteger.valueOf(0));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithAbsentDownloadsCount() {
    parse(newTorrentPreviewTrBySampleId("with-absent-downloads-count"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void throwsOnElementWithInvalidDownloadsCount() {
    parse(newTorrentPreviewTrBySampleId("with-invalid-downloads-count"));
  }

  /* Utility Methods */

  private Tr newTorrentPreviewTrBySampleId(final String sampleId) {
    val selector = String.format("tr[data-sample-id='%s']", sampleId);
    return trSamplesDoc.select(selector, Tr::new).get(0);
  }

  private TorrentPreview parse(final Tr torrentPreviewTr) {
    return parser.parse(torrentPreviewTr);
  }
}
