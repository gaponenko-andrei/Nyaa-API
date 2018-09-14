package agp.nyaa.api.parser;

import agp.nyaa.api.exception.parse.ParseException;
import agp.nyaa.api.exception.parse.TorrentPreviewParseException;
import agp.nyaa.api.mapper.*;
import agp.nyaa.api.model.*;
import agp.nyaa.api.util.NyaaLogWriter;
import lombok.NonNull;
import lombok.val;
import org.jsoup.nodes.Element;

import java.net.URI;

import static com.google.common.base.Preconditions.checkArgument;

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

    return torrentPreviewBuilder.build();
  }

  private static Long parseIdOf(final Element torrentPreviewElement) {
    val column = 1;
    val idColumn = torrentPreviewElement.select("td").get(column);
    val idString = idColumn.select("a").attr("href").replace("/view/", "");
    return Long.valueOf(idString);
  }

  private static TorrentState parseStateOf(final Element torrentPreviewElement) {
    val torrentCssCategory = torrentPreviewElement.attr("class");
    return new TorrentStateMapper().map(torrentCssCategory);
  }

  private static Category parseCategoryOf(final Element torrentPreviewElement) {
    val column = 0;
    val categoryColumn = torrentPreviewElement.select("td").get(column);
    val categoryLink = categoryColumn.selectFirst("a");
    return new CategoryMapper().map(categoryLink.attr("href"));
  }

  private static String parseTitleOf(final Element torrentPreviewElement) {
    val column = 1;
    val titleColumn = torrentPreviewElement.select("td").get(column);
    val titleLink = titleColumn.selectFirst("a");
    return titleLink.attr("title");
  }

  private static URI parseDownloadLinkOf(final Element torrentPreviewElement) {
    val column = 2;
    val downloadLinksColumn = torrentPreviewElement.select("td").get(column);
    val torrentDownloadHref = downloadLinksColumn.selectFirst("a").attr("href");
    return new StringToUriMapper().map(torrentDownloadHref);
  }

  private static URI parseMagnetLinkOf(final Element torrentPreviewElement) {
    val column = 2;
    val downloadLinksColumn = torrentPreviewElement.select("td").get(column);
    val torrentDownloadHref = downloadLinksColumn.select("a").get(1).attr("href");
    return new StringToUriMapper().map(torrentDownloadHref);
  }

  private static DataSize parseDataSizeOf(final Element torrentPreviewElement) {
    val dataSizeColumn = getColumn(torrentPreviewElement, 3);
    val dataSize = dataSizeColumn.text();
    return new DataSizeMapper(DataSizeUnitMapper.impl()).map(dataSize);
  }

  private static Element getColumn(final Element torrentPreviewElement, final int columnIndex) {
    return torrentPreviewElement.select("td").get(columnIndex);
  }

  private static ParseException newDetailedLoggedException(final Exception failureCause,
                                                           final Element torrentPreviewElement) {

    val detailedException = new TorrentPreviewParseException(torrentPreviewElement, failureCause);
    LOGGER.log(detailedException);
    return detailedException;
  }
}