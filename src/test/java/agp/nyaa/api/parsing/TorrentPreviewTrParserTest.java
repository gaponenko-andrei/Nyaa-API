package agp.nyaa.api.parsing;

import static org.testng.Assert.assertEquals;

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

public class TorrentPreviewTrParserTest {

  private TorrentPreviewTrParser parser = new TorrentPreviewTrParser();
  private ImmutableDocument trSamplesDoc = TestDocuments.get("torrent-preview-tr-samples.html");
  private Tr validTorrentPreviewTr = newTorrentPreviewTr("valid");


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void parserShouldThrowOnNulls() {
    parse(null);
  }

  /* id */

  @Test
  public void parsingShouldProduceExpectedId() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.id().longValue(), 1032497L);
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithoutId() {
    parse(newTorrentPreviewTr("with-absent-id"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithInvalidId() {
    parse(newTorrentPreviewTr("with-invalid-id"));
  }

  /* state */

  @Test
  public void parsingShouldProduceExpectedState() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.state(), TorrentState.NORMAL);
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithoutState() {
    parse(newTorrentPreviewTr("with-absent-state"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithUnknownState() {
    parse(newTorrentPreviewTr("with-unknown-state"));
  }

  /* category */

  @Test
  public void parsingShouldProduceExpectedCategory() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.category(), Category.Anime.EnglishTranslated.instance());
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithoutCategory() {
    parse(newTorrentPreviewTr("with-absent-category"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithUnknownCategory() {
    parse(newTorrentPreviewTr("with-unknown-category"));
  }

  /* title */

  @Test
  public void parsingShouldProduceExpectedTitle() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.title(), "[Erai-raws] Tokyo Ghoul-re - 05 [720p].mkv");
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithoutTitle() {
    parse(newTorrentPreviewTr("with-absent-title"));
  }

  /* torrent download uri */

  @Test
  public void parsingShouldProduceExpectedTorrentDownloadLink() {
    val result = parse(validTorrentPreviewTr);
    val expectedResult = StringToUriMapper.applicationTo("/download/1032497.torrent");
    assertEquals(result.downloadLink(), expectedResult);
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithoutTorrentDownloadLink() {
    parse(newTorrentPreviewTr("with-absent-torrent-download-uri"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithInvalidTorrentDownloadLink() {
    parse(newTorrentPreviewTr("with-invalid-torrent-download-uri"));
  }

  /* magnet uri */

  @Test
  public void parsingShouldProduceExpectedMagnetLink() {
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
  public void parsingShouldThrowOnTrWithoutMagnetLink() {
    parse(newTorrentPreviewTr("with-absent-magnet-uri"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithInvalidMagnetLink() {
    parse(newTorrentPreviewTr("with-invalid-magnet-uri"));
  }

  /* upload instant */

  @Test
  public void parsingShouldProduceExpectedUploadInstant() {

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
  public void parsingShouldThrowOnTrWithoutUploadInstant() {
    parse(newTorrentPreviewTr("with-absent-upload-instant"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithInvalidUploadInstant() {
    parse(newTorrentPreviewTr("with-invalid-upload-instant"));
  }

  /* seeders count */

  @Test
  public void parsingShouldProduceExpectedSeedersCount() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.seedersCount(), UnsignedInteger.valueOf(1));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithoutSeedersCount() {
    parse(newTorrentPreviewTr("with-absent-seeders-count"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithInvalidSeedersCount() {
    parse(newTorrentPreviewTr("with-invalid-seeders-count"));
  }

  /* leechers count */

  @Test
  public void parsingShouldProduceExpectedLeechersCount() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.leechersCount(), UnsignedInteger.valueOf(39));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithoutLeechersCount() {
    parse(newTorrentPreviewTr("with-absent-leechers-count"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithInvalidLeechersCount() {
    parse(newTorrentPreviewTr("with-invalid-leechers-count"));
  }

  /* downloads count */

  @Test
  public void parsingShouldProduceExpectedDownloadsCount() {
    val result = parse(validTorrentPreviewTr);
    assertEquals(result.downloadsCount(), UnsignedInteger.valueOf(0));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithoutDownloadsCount() {
    parse(newTorrentPreviewTr("with-absent-downloads-count"));
  }

  @Test(expectedExceptions = TorrentPreviewTrParser.Exception.class)
  public void parsingShouldThrowOnTrWithInvalidDownloadsCount() {
    parse(newTorrentPreviewTr("with-invalid-downloads-count"));
  }

  /* Utility Methods */

  private Tr newTorrentPreviewTr(final String sampleId) {
    val selector = String.format("tr[data-sample-id='%s']", sampleId);
    return trSamplesDoc.select(selector, Tr::new).get(0);
  }

  private TorrentPreview parse(final Tr torrentPreviewTr) {
    return parser.parse(torrentPreviewTr);
  }
}
