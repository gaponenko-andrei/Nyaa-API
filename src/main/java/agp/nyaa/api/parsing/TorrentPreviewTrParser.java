package agp.nyaa.api.parsing;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.google.common.primitives.UnsignedInteger;

import agp.nyaa.api.element.Link;
import agp.nyaa.api.element.Td;
import agp.nyaa.api.element.Tr;
import agp.nyaa.api.exception.parse.ParseException;
import agp.nyaa.api.mapping.CategoryMapping;
import agp.nyaa.api.mapping.DataSizeMapping;
import agp.nyaa.api.mapping.DataSizeUnitMapping;
import agp.nyaa.api.mapping.StringToUriMapping;
import agp.nyaa.api.mapping.TorrentStateMapping;
import agp.nyaa.api.model.Category;
import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentState;
import agp.nyaa.api.util.NyaaLogWriter;
import lombok.NonNull;
import lombok.val;

public class TorrentPreviewTrParser implements Parser<Tr, TorrentPreview> {

  private static final NyaaLogWriter LOGGER = NyaaLogWriter.of(TorrentPreviewTrParser.class);

  @Override
  public TorrentPreview apply(@NonNull final Tr torrentPreviewTr) {
    try {
      return tryParse(torrentPreviewTr);
    } catch (java.lang.Exception ex) {
      throw newDetailedLoggedException(ex, torrentPreviewTr);
    }
  }

  private TorrentPreview tryParse(final Tr torrentPreviewTr) {
    val torrentPreviewBuilder = TorrentPreview.builder();

    // parse and set id
    val torrentId = parseIdOf(torrentPreviewTr);
    torrentPreviewBuilder.id(torrentId);

    // parse and set state
    val torrentState = parseStateOf(torrentPreviewTr);
    torrentPreviewBuilder.state(torrentState);

    // parse and set category
    val torrentCategory = parseCategoryOf(torrentPreviewTr);
    torrentPreviewBuilder.category(torrentCategory);

    // parse and set title
    val torrentTitle = parseTitleOf(torrentPreviewTr);
    torrentPreviewBuilder.title(torrentTitle);

    // parse and set torrent download link
    val torrentDownloadLink = parseDownloadLinkOf(torrentPreviewTr);
    torrentPreviewBuilder.downloadLink(torrentDownloadLink);

    // parse and set torrent magnet link
    val torrentMagnetLink = parseMagnetLinkOf(torrentPreviewTr);
    torrentPreviewBuilder.magnetLink(torrentMagnetLink);

    // parse and set torrent data size
    val torrentDataSize = parseDataSizeOf(torrentPreviewTr);
    torrentPreviewBuilder.dataSize(torrentDataSize);

    // parse and set upload date
    val uploadInstant = parseUploadInstantOf(torrentPreviewTr);
    torrentPreviewBuilder.uploadInstant(uploadInstant);

    // parse and set seeders count
    val seedersCount = parseSeedersCountOf(torrentPreviewTr);
    torrentPreviewBuilder.seedersCount(seedersCount);

    // parse and set leechers count
    val leechersCount = parseLeechersCountOf(torrentPreviewTr);
    torrentPreviewBuilder.leechersCount(leechersCount);

    // parse and set downloads count
    val downloadsCount = parseDownloadsCountOf(torrentPreviewTr);
    torrentPreviewBuilder.downloadsCount(downloadsCount);

    return torrentPreviewBuilder.build();
  }

  private static Long parseIdOf(final Tr torrentPreviewTr) {
    val idTd = td(torrentPreviewTr, 1);
    val idLink = link(idTd, 0);
    val idString = idLink.attr("href").replace("/view/", "");
    return Long.valueOf(idString);
  }

  private static TorrentState parseStateOf(final Tr torrentPreviewTr) {
    val torrentCategoryString = torrentPreviewTr.attr("class");
    return TorrentStateMapping.applicationTo(torrentCategoryString);
  }

  private static Category parseCategoryOf(final Tr torrentPreviewTr) {
    val categoryTd = td(torrentPreviewTr, 0);
    val categoryLink = link(categoryTd, 0);
    return CategoryMapping.applicationTo(categoryLink.attr("href"));
  }

  private static String parseTitleOf(final Tr torrentPreviewTr) {
    val titleTd = td(torrentPreviewTr, 1);
    val titleLink = link(titleTd, 0);
    return titleLink.attr("title");
  }

  private static URI parseDownloadLinkOf(final Tr torrentPreviewTr) {
    val downloadTd = td(torrentPreviewTr, 2);
    val downloadLink = link(downloadTd, 0);
    return StringToUriMapping.applicationTo(downloadLink.attr("href"));
  }

  private static URI parseMagnetLinkOf(final Tr torrentPreviewTr) {
    val magnetTd = td(torrentPreviewTr, 2);
    val magnetLink = link(magnetTd, 1);
    return StringToUriMapping.applicationTo(magnetLink.attr("href"));
  }

  private static DataSize parseDataSizeOf(final Tr torrentPreviewTr) {
    val dataSizeTd = td(torrentPreviewTr, 3);
    return DataSizeMapping.using(DataSizeUnitMapping.impl()).apply(dataSizeTd.text());
  }

  private static Instant parseUploadInstantOf(final Tr torrentPreviewTr) {
    val uploadInstantTd = td(torrentPreviewTr, 4);
    val dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return LocalDateTime.parse(uploadInstantTd.text(), dateTimePattern).toInstant(ZoneOffset.UTC);
  }

  private static UnsignedInteger parseSeedersCountOf(final Tr torrentPreviewTr) {
    val seedersCountTd = td(torrentPreviewTr, 5);
    return UnsignedInteger.valueOf(seedersCountTd.text());
  }

  private static UnsignedInteger parseLeechersCountOf(final Tr torrentPreviewTr) {
    val leechersCountTd = td(torrentPreviewTr, 6);
    return UnsignedInteger.valueOf(leechersCountTd.text());
  }

  private static UnsignedInteger parseDownloadsCountOf(final Tr torrentPreviewTr) {
    val downloadsCountTd = td(torrentPreviewTr, 7);
    return UnsignedInteger.valueOf(downloadsCountTd.text());
  }

  private static Td td(final Tr torrentPreviewTr, int index) {
    return torrentPreviewTr.select("td", Td::new).get(index);
  }

  private static Link link(final Td torrentPreviewTd, int index) {
    return torrentPreviewTd.select("a", Link::new).get(index);
  }

  private static Exception newDetailedLoggedException(final java.lang.Exception cause,
                                                      final Tr torrentPreviewTr) {

    val detailedException = new Exception(torrentPreviewTr, cause);
    LOGGER.log(detailedException);
    return detailedException;
  }

  static class Exception extends ParseException {

    Exception(final Tr torrentPreviewTr, final Throwable cause) {
      super(newMessageFor(torrentPreviewTr), cause);
    }

    private static String newMessageFor(final Tr invalidTr) {
      return "Failed to parse torrent preview <tr>: " + System.lineSeparator() + invalidTr;
    }
  }
}
