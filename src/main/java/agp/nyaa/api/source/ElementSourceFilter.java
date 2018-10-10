package agp.nyaa.api.source;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jsoup.nodes.Element;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
abstract class ElementSourceFilter
  <T extends Element, U extends Element>
  implements ElementSource<U> {

  @NonNull
  private final ElementSource<T> elementSource;

  @Override
  public final U get(@NonNull final String relativePath) {
    val element = elementSource.get(relativePath);
    return filter(element);
  }

  abstract U filter(T element);
}
