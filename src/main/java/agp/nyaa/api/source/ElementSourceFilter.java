package agp.nyaa.api.source;

import lombok.NonNull;
import lombok.val;
import org.jsoup.nodes.Element;

abstract class ElementSourceFilter<T extends Element, U extends Element> extends ElementSource<U> {

  @NonNull
  private final ElementSource<T> elementSource;

  @Override
  public final U get(@NonNull final String relativePath) {
    val element = elementSource.get(relativePath);
    return filter(element);
  }

  ElementSourceFilter(@NonNull final ElementSource<T> elementSource) {
    super(elementSource.baseUri());
    this.elementSource = elementSource;
  }

  abstract U filter(T element);
}
