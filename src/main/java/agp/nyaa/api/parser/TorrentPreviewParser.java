package agp.nyaa.api.parser;

import agp.nyaa.api.exception.parse.AbsentAttributeException;
import agp.nyaa.api.exception.parse.ParseException;
import agp.nyaa.api.exception.parse.TorrentPreviewParseException;
import agp.nyaa.api.mapper.*;
import agp.nyaa.api.model.*;
import agp.nyaa.api.util.NyaaLogWriter;
import lombok.NonNull;
import lombok.val;
import org.jsoup.nodes.Element;

import javax.management.AttributeNotFoundException;
import javax.print.AttributeException;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.google.common.base.Preconditions.checkArgument;

// TODO проверять отсутствие атрибутов

public final class TorrentPreviewParser implements Parser<Element, TorrentPreview> {

  private static final NyaaLogWriter LOGGER = NyaaLogWriter.of(TorrentPreviewParser.class);

  @Override
  public TorrentPreview apply(@NonNull final Element torrentPreviewElement) {
    checkArgument(torrentPreviewElement.is("tr"),
                  "Provided element is not a table row, thus it " +
                    "cannot be parsed as a torrent preview element.");
    try {
      return tryParse(torrentPreviewElement);
    } catch (Exception ex) {
      throw newDetailedLoggedException(ex, torrentPreviewElement);
    }
  }

  private TorrentPreview tryParse(final Element torrentPreviewElement) {
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

    // parse torrent download link
    val torrentDownloadLink = parseDownloadLinkOf(torrentPreviewElement);
    torrentPreviewBuilder.downloadLink(torrentDownloadLink);

    // parse torrent magnet link
    val torrentMagnetLink = parseMagnetLinkOf(torrentPreviewElement);
    torrentPreviewBuilder.magnetLink(torrentMagnetLink);

    // parse torrent data size
    val torrentDataSize = parseDataSizeOf(torrentPreviewElement);
    torrentPreviewBuilder.dataSize(torrentDataSize);

    // parse upload date
    val uploadDate = parseUploadDateOf(torrentPreviewElement);
    torrentPreviewBuilder.uploadDate(uploadDate);


    return torrentPreviewBuilder.build();
  }

  private static Long parseIdOf(final Element torrentPreviewElement) {
    val idColumn = getColumn(torrentPreviewElement, 1);
    val idSourceElement = idColumn.selectFirst("a");
    val hrefValue = getAttributeValue(idSourceElement, "href");
    val idString = hrefValue.replace("/view/", "");
    return Long.valueOf(idString);
  }

  private static TorrentState parseStateOf(final Element torrentPreviewElement) {
    val torrentCssCategory = getAttributeValue(torrentPreviewElement, "class");
    return new TorrentStateMapper().map(torrentCssCategory);
  }

  private static Category parseCategoryOf(final Element torrentPreviewElement) {
    val categoryColumn = getColumn(torrentPreviewElement, 0);
    val categoryLink = categoryColumn.selectFirst("a");
    val categoryString = getAttributeValue(categoryLink, "href");
    return new CategoryMapper().map(categoryString);
  }

  private static String parseTitleOf(final Element torrentPreviewElement) {
    val titleColumn = getColumn(torrentPreviewElement, 1);
    val titleLink = titleColumn.selectFirst("a");
    return getAttributeValue(titleLink, "title");
  }

  private static URI parseDownloadLinkOf(final Element torrentPreviewElement) {
    val downloadLinksColumn = getColumn(torrentPreviewElement, 2);
    val downloadLink = downloadLinksColumn.selectFirst("a");
    val torrentDownloadHref = getAttributeValue(downloadLink, "href");
    return StringToUriMapper.applicationTo(torrentDownloadHref);
  }

  private static URI parseMagnetLinkOf(final Element torrentPreviewElement) {
    val downloadLinksColumn = getColumn(torrentPreviewElement, 2);
    val magnetLink = downloadLinksColumn.select("a").get(1);
    val torrentMagnetHref = getAttributeValue(magnetLink, "href");
    return StringToUriMapper.applicationTo(torrentMagnetHref);
  }

  private static DataSize parseDataSizeOf(final Element torrentPreviewElement) {
    val dataSizeColumn = getColumn(torrentPreviewElement, 3);
    val dataSize = getTextOf(dataSizeColumn);
    return DataSizeMapper.using(DataSizeUnitMapper.impl()).map(dataSize);
  }

  private static Instant parseUploadDateOf(final Element torrentPreviewElement) {
    val uploadDateColumn = getColumn(torrentPreviewElement, 4);
    val uploadDateString = getTextOf(uploadDateColumn);
    val dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    val localDateTime = LocalDateTime.parse(uploadDateString, dateTimePattern);
    return ZonedDateTime.of(localDateTime, ZoneId.of("UTC")).toInstant();
  }

  private static Element getColumn(final Element torrentPreviewElement, final int columnIndex) {
    return torrentPreviewElement.select("td").get(columnIndex);
  }

  private static String getAttributeValue(final Element attributeOwner, final String attribute) {
    if (attributeOwner.hasAttr(attribute)) {
      return attributeOwner.attr(attribute);
    } else {
      throw new AbsentAttributeException(String.format(
        "Attribute '%s' not found.", attribute));
    }
  }

  private static String getTextOf(final Element element) {
    if (element.hasText()) {
      return element.text();
    } else {
      throw new ParseException("Element has no text.");
    }
  }

  private static ParseException newDetailedLoggedException(final Exception failureCause,
                                                           final Element torrentPreviewElement) {

    val detailedException = new TorrentPreviewParseException(torrentPreviewElement, failureCause);
    LOGGER.log(detailedException);
    return detailedException;
  }
}
