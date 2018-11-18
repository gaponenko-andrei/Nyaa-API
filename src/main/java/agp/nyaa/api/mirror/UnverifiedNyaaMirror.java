package agp.nyaa.api.mirror;

import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;

import java.net.URL;

import org.immutables.value.Value;

import lombok.NonNull;

@Value.Immutable(builder = false)
@Value.Style(
  of = "aka",
  visibility = PACKAGE,
  overshadowImplementation = true
)
public interface UnverifiedNyaaMirror extends NyaaMirror {

  @Value.Parameter
  URL url();

  static UnverifiedNyaaMirror aka(@NonNull final URL url) {
    return ImmutableUnverifiedNyaaMirror.aka(url);
  }
}

