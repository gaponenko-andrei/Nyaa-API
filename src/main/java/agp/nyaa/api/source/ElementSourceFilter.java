package agp.nyaa.api.source;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jsoup.nodes.Element;

import java.nio.file.Path;

@RequiredArgsConstructor
abstract class ElementSourceFilter<T extends Element, U extends Element> implements ElementSource<U> {

  @NonNull
  private final ElementSource<T> elementSource;

  @Override
  public final U getElementBy(@NonNull final Path path) {
    val element = elementSource.getElementBy(path);
    return filter(element);
  }

  abstract U filter(T element);
}
