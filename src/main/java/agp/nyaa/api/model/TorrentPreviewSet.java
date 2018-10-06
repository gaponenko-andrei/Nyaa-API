package agp.nyaa.api.model;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@RequiredArgsConstructor
public final class TorrentPreviewSet extends ForwardingSet<TorrentPreview> {

  @Getter
  private final ImmutableSet<TorrentPreview> delegate;


  public TorrentPreviewSet(@NonNull final Set<TorrentPreview> delegate) {
    this.delegate = ImmutableSet.copyOf(delegate);
  }

  public Set<Long> ids() {
    return delegate.stream().map(TorrentPreview::id).collect(Collectors.toSet());
  }
}
