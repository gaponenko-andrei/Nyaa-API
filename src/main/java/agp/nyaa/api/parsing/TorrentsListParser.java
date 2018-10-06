package agp.nyaa.api.parsing;

import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentPreviewSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor(staticName = "using")
public final class TorrentsListParser implements Parser<Element, TorrentPreviewSet> {

  @Getter
  @NonNull
  private final Parser<Element, TorrentPreview> torrentPreviewParser;

  @Override
  public TorrentPreviewSet apply(@NonNull final Element torrentsListElement) {
    checkArgument(torrentsListElement.is("table.torrent-list"),
                  "Provided element is not a 'table.torrent-list'.");

    val torrentPreviewTrs = torrentsListElement.select("tbody tr");

    return new TorrentPreviewSet(
      torrentPreviewTrs
        .stream()
        .map(torrentPreviewParser)
        .collect(toSet())
    );
  }
}
