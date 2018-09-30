package agp.nyaa.api.model;

import com.google.common.primitives.UnsignedInteger;
import org.immutables.value.Value;

import java.net.URI;
import java.time.Instant;

import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;

@Immutable
@Value.Style(visibility = PACKAGE, overshadowImplementation = true)
public abstract class TorrentPreview {

  public abstract Long id();
  public abstract TorrentState state();
  public abstract Category category();
  public abstract String title();
  public abstract URI downloadLink();
  public abstract URI magnetLink();
  public abstract DataSize dataSize();
  public abstract Instant uploadInstant();
  public abstract UnsignedInteger seedersCount();
  public abstract UnsignedInteger leechersCount();

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends ImmutableTorrentPreview.Builder {}
}
