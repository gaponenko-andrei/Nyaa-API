package agp.nyaa.api.mapper;

import agp.nyaa.api.model.TorrentState;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import properties.Constants;

import static agp.nyaa.api.model.TorrentState.*;
import static com.google.common.base.Preconditions.checkArgument;

public abstract class TorrentStateMapper<T, R> implements Mapper<T, R> {

  public static Mapper<TorrentState, String> toCssClass() {
    return new ToCssClassMapper();
  }

  public static Mapper<String, TorrentState> fromCssClass() {
    return new FromCssClassMapper();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  private static final class ToCssClassMapper extends TorrentStateMapper<TorrentState, String> {

    private static final ImmutableBiMap<TorrentState, String> MAPPING =
            ImmutableBiMap.<TorrentState, String>
                    builder()
                    .put(NORMAL, Constants.torrent.state.normal.className)
                    .put(REMAKE, Constants.torrent.state.danger.className)
                    .put(TRUSTED, Constants.torrent.state.trusted.className)
                    .build();

    @Override
    public String apply(@NonNull final TorrentState torrentState) {
      return MAPPING.get(torrentState);
    }
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  private static final class FromCssClassMapper extends TorrentStateMapper<String, TorrentState> {

    private static final ImmutableMap<String, TorrentState> MAPPING =
            ImmutableMap.<String, TorrentState>builder()
                    .put(Constants.torrent.state.normal.className, NORMAL)
                    .put(Constants.torrent.state.danger.className, REMAKE)
                    .put(Constants.torrent.state.trusted.className, TRUSTED)
                    .build();

    @Override
    public TorrentState apply(@NonNull final String cssClass) {
      checkArgument(isKnown(cssClass), "Unknown 'cssClass': %s.");
      return MAPPING.get(cssClass);
    }

    private static boolean isKnown(final String cssClass) {
      return MAPPING.containsKey(cssClass);
    }
  }
}
