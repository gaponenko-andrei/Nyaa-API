package agp.nyaa.api.source;

import org.jsoup.nodes.Element;

import java.net.URI;

public interface ElementSource<T extends Element> {
  T getElementBy(URI uri);
}
