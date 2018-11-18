package agp.nyaa.api.mapper;

import agp.nyaa.api.model.TorrentState;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;

import static agp.nyaa.api.model.TorrentState.*;
import static com.google.common.base.Preconditions.checkArgument;

public final class TorrentStateMapper implements SupportedValuesAwareMapper<String, TorrentState> {

  private static final ImmutableMap<String, TorrentState> MAPPING =
    ImmutableMap.<String, TorrentState>builder()
      .put("default", NORMAL)
      .put("danger", REMAKE)
      .put("success", TRUSTED)
      .build();


  public static TorrentState applicationTo(@NonNull final String cssClass) {
    return new TorrentStateMapper().apply(cssClass);
  }

  @Override
  public TorrentState apply(@NonNull final String cssClass) {
    checkArgument(isSupported(cssClass), "Unsupported 'cssClass': %s.", cssClass);
    return MAPPING.get(cssClass);
  }

  @Override
  public ImmutableSet<String> supportedValues() {
    return MAPPING.keySet();
  }
}
