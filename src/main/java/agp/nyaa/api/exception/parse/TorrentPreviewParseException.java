package agp.nyaa.api.exception.parse;

import lombok.val;
import org.jsoup.nodes.Element;

import static java.lang.String.format;

public class TorrentPreviewParseException extends ParseException {

  public TorrentPreviewParseException(final Element torrentPreview,
                                      final Throwable throwableCause) {

    super(newMessageFor(torrentPreview), throwableCause);
  }

  private static String newMessageFor(final Element torrentPreviewElement) {
    val template = "Failed to parse torrent preview element: %s%s";
    return format(template, System.lineSeparator(), torrentPreviewElement);
  }
}
