package agp.nyaa.api.source;

import org.jsoup.nodes.Element;

import java.nio.file.Path;

public interface ElementSource<T extends Element> {
  T getElementBy(Path path);
}
