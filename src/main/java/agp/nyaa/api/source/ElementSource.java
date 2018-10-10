package agp.nyaa.api.source;

import org.jsoup.nodes.Element;

public interface ElementSource<E extends Element> {
  E get(final String relativePath);
}
