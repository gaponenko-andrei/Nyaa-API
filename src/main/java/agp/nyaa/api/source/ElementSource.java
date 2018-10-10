package agp.nyaa.api.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jsoup.nodes.Element;

import java.net.URI;

@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class ElementSource<E extends Element> {

  @Getter
  private final URI baseUri;

  public abstract E get(final String relativePath);
}
