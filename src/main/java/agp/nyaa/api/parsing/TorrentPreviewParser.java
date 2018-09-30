package agp.nyaa.api.parsing;

import agp.nyaa.api.exception.parse.ParseException;
import agp.nyaa.api.exception.parse.TorrentPreviewParseException;
import agp.nyaa.api.mapper.*;
import agp.nyaa.api.model.Category;
import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentState;
import agp.nyaa.api.util.NyaaLogWriter;
import com.google.common.primitives.UnsignedInteger;
import lombok.NonNull;
import lombok.val;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.google.common.base.Preconditions.checkArgument;

public final class TorrentPreviewParser implements Parser<Element, TorrentPreview> {

  private static final NyaaLogWriter LOGGER = NyaaLogWriter.of(TorrentPreviewParser.class);

  @Override
  public TorrentPreview apply(@NonNull final Element torrentPreviewElement) {
    checkArgument(torrentPreviewElement.is("tr"),
                  "Provided element is not a table row, thus it " +
                    "cannot be parsed as a torrent preview element.");
    try {
      return tryParse(Tr.of(torrentPreviewElement));
    } catch (Exception ex) {
      throw newDetailedLoggedException(ex, torrentPreviewElement);
    }
  }

  private TorrentPreview tryParse(final Tr torrentPreviewElement) {
    val torrentPreviewBuilder = TorrentPreview.builder();

    // parse and set id
    val torrentId = parseIdOf(torrentPreviewElement);
    torrentPreviewBuilder.id(torrentId);

    // parse and set state
    val torrentState = parseStateOf(torrentPreviewElement);
    torrentPreviewBuilder.state(torrentState);

    // parse and set category
    val torrentCategory = parseCategoryOf(torrentPreviewElement);
    torrentPreviewBuilder.category(torrentCategory);

    // parse and set title
    val torrentTitle = parseTitleOf(torrentPreviewElement);
    torrentPreviewBuilder.title(torrentTitle);

    // parse and set torrent download link
    val torrentDownloadLink = parseDownloadLinkOf(torrentPreviewElement);
    torrentPreviewBuilder.downloadLink(torrentDownloadLink);

    // parse and set torrent magnet link
    val torrentMagnetLink = parseMagnetLinkOf(torrentPreviewElement);
    torrentPreviewBuilder.magnetLink(torrentMagnetLink);

    // parse and set torrent data size
    val torrentDataSize = parseDataSizeOf(torrentPreviewElement);
    torrentPreviewBuilder.dataSize(torrentDataSize);

    // parse and set upload date
    val uploadInstant = parseUploadInstantOf(torrentPreviewElement);
    torrentPreviewBuilder.uploadInstant(uploadInstant);

    // parse and set seeders count
    val seedersCount = parseSeedersCountOf(torrentPreviewElement);
    torrentPreviewBuilder.seedersCount(seedersCount);

    // parse and set leechers count
    val leechersCount = parseLeechersCountOf(torrentPreviewElement);
    torrentPreviewBuilder.leechersCount(leechersCount);

    return torrentPreviewBuilder.build();
  }

  private static Long parseIdOf(final Tr torrentPreviewTr) {
    val idString = torrentPreviewTr.td(1).link(0).href().replace("/view/", "");
    return Long.valueOf(idString);
  }

  private static TorrentState parseStateOf(final Tr torrentPreviewTr) {
    val torrentCssCategory = torrentPreviewTr.cssClass();
    return TorrentStateMapper.applicationTo(torrentCssCategory);
  }

  private static Category parseCategoryOf(final Tr torrentPreviewTr) {
    val categoryString = torrentPreviewTr.td(0).link(0).href();
    return CategoryMapper.applicationTo(categoryString);
  }

  private static String parseTitleOf(final Tr torrentPreviewTr) {
    return torrentPreviewTr.td(1).link(0).title();
  }

  private static URI parseDownloadLinkOf(final Tr torrentPreviewTr) {
    val torrentDownloadHref = torrentPreviewTr.td(2).link(0).href();
    return StringToUriMapper.applicationTo(torrentDownloadHref);
  }

  private static URI parseMagnetLinkOf(final Tr torrentPreviewTr) {
    val torrentMagnetHref = torrentPreviewTr.td(2).link(1).href();
    return StringToUriMapper.applicationTo(torrentMagnetHref);
  }

  private static DataSize parseDataSizeOf(final Tr torrentPreviewTr) {
    val dataSize = torrentPreviewTr.td(3).text();
    return DataSizeMapper.using(DataSizeUnitMapper.impl()).map(dataSize);
  }

  private static Instant parseUploadInstantOf(final Tr torrentPreviewTr) {
    val uploadInstantString = torrentPreviewTr.td(4).text();
    val dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return LocalDateTime.parse(uploadInstantString, dateTimePattern).toInstant(ZoneOffset.UTC);
  }

  private static UnsignedInteger parseSeedersCountOf(final Tr torrentPreviewTr) {
    val seedersCountString = torrentPreviewTr.td(5).text();
    return UnsignedInteger.valueOf(seedersCountString);
  }

  private static UnsignedInteger parseLeechersCountOf(final Tr torrentPreviewTr) {
    val leechersCountString = torrentPreviewTr.td(6).text();
    return UnsignedInteger.valueOf(leechersCountString);
  }

  private static ParseException newDetailedLoggedException(final Exception failureCause,
                                                           final Element torrentPreviewElement) {

    val detailedException = new TorrentPreviewParseException(torrentPreviewElement, failureCause);
    LOGGER.log(detailedException);
    return detailedException;
  }
}
