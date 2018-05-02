package agp.nyaa.api.mapper;

import agp.nyaa.api.model.TorrentState;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static agp.nyaa.api.model.TorrentState.*;
import static com.google.common.base.Preconditions.checkArgument;

public abstract class TorrentStateMapper<T, R> implements Mapper<T, R> {

  public static TorrentStateMapper<String, TorrentState> fromCssClass() {
    return new FromCssClassMapper();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  private static final class FromCssClassMapper extends TorrentStateMapper<String, TorrentState> {

    private static final ImmutableMap<String, TorrentState> MAPPING =
            ImmutableMap.<String, TorrentState>builder()
                    .put("default", NORMAL)
                    .put("danger", REMAKE)
                    .put("success", TRUSTED)
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
