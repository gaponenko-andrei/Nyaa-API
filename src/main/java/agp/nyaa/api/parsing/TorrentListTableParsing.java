package agp.nyaa.api.parsing;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import agp.nyaa.api.element.Table;
import agp.nyaa.api.element.Tr;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentPreviewSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(staticName = "using")
public final class TorrentListTableParsing implements Function<Table, TorrentPreviewSet> {

  @Getter
  @NonNull
  private final Function<Tr, TorrentPreview> torrentPreviewParsing;

  @Override
  public TorrentPreviewSet apply(@NonNull final Table torrentListTable) {
    validate(torrentListTable);
    val torrentPreviewTrs = selectTorrentPreviewTrsFrom(torrentListTable);
    val torrentPreviewSet = parse(torrentPreviewTrs);
    return new TorrentPreviewSet(torrentPreviewSet);
  }

  private List<Tr> selectTorrentPreviewTrsFrom(final Table torrentListTable) {
    return torrentListTable.select("tbody tr", Tr::new);
  }

  private Set<TorrentPreview> parse(final List<Tr> torrentPreviewTrs) {
    return torrentPreviewTrs.stream().map(torrentPreviewParsing).collect(toSet());
  }

  private static void validate(final Table element) {
    checkArgument(element.hasClass("torrent-list"), "Table must have 'torrent-list' class.");
  }
}
