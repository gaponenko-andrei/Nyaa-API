package agp.nyaa.api.mapping;

import java.net.URI;
import java.util.function.Function;

import agp.nyaa.api.util.Try;
import lombok.NonNull;

public final class StringToUriMapping implements Function<String, URI> {

  public static URI applicationTo(@NonNull final String uriString) {
    return new StringToUriMapping().apply(uriString);
  }

  @Override
  public URI apply(@NonNull final String uriString) {
    return Try.call(() -> new URI(uriString)).orElseThrow(IllegalArgumentException::new);
  }
}
