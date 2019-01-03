package agp.nyaa.api.parsing;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;

import agp.nyaa.api.element.Table;
import agp.nyaa.api.element.Tr;
import agp.nyaa.api.model.TorrentPreview;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(staticName = "using")
public final class TorrentListTableParsing implements Function<Table, ImmutableSet<TorrentPreview>> {

  @Getter
  @NonNull
  private final Function<Tr, TorrentPreview> torrentPreviewParsing;

  @Override
  public ImmutableSet<TorrentPreview> apply(@NonNull final Table torrentListTable) {
    validate(torrentListTable);
    val torrentPreviewTrs = selectTorrentPreviewTrsFrom(torrentListTable);
    return parse(torrentPreviewTrs);
  }

  private static void validate(final Table element) {
    checkArgument(element.hasClass("torrent-list"), "Table must have 'torrent-list' class.");
  }

  private List<Tr> selectTorrentPreviewTrsFrom(final Table torrentListTable) {
    return torrentListTable.select("tbody tr", Tr::new);
  }

  private ImmutableSet<TorrentPreview> parse(final List<Tr> torrentPreviewTrs) {
    return torrentPreviewTrs.stream().map(torrentPreviewParsing).collect(toImmutableSet());
  }
}
