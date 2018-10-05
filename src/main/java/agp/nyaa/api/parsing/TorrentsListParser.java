package agp.nyaa.api.parsing;

import agp.nyaa.api.model.TorrentPreview;
import lombok.NonNull;
import org.jsoup.nodes.Element;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

public final class TorrentsListParser implements Parser<Element, Set<TorrentPreview>> {

  @Override
  public Set<TorrentPreview> apply(@NonNull final Element torrentsListElement) {
    checkArgument(torrentsListElement.is("table.torrent-list"),
                  "Provided element is not a 'table.torrent-list'.");

    return null;
  }

}
