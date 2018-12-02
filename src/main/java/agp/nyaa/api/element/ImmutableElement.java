package agp.nyaa.api.element;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.lang.String.format;

import java.net.URI;
import java.util.function.Function;

import org.jsoup.nodes.Element;

import com.google.common.collect.ImmutableList;

import agp.nyaa.api.exception.NoSuchAttributeException;
import agp.nyaa.api.mapping.StringToUriMapping;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@javax.annotation.concurrent.Immutable
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ImmutableElement<T extends Element> {

  @NonNull T delegate;
  @NonNull URI uri;

  @SuppressWarnings("unchecked")
  ImmutableElement(@NonNull final T sourceElement) {
    this.delegate = (T) sourceElement.clone();
    this.uri = StringToUriMapping.applicationTo(delegate.baseUri());
  }

  public final URI uri() {
    return uri;
  }

  public final String text() {
    return delegate.text();
  }

  @Override
  public final String toString() {
    return delegate.toString();
  }

  public final boolean has(@NonNull final String selector) {
    return delegate.select(selector).size() > 0;
  }

  public final boolean hasClass(@NonNull final String cssClass) {
    return delegate.hasClass(cssClass);
  }

  public final boolean hasAttr(@NonNull final String attribute) {
    return delegate.hasAttr(attribute);
  }

  public final String attr(@NonNull final String attribute) {
    if (delegate.hasAttr(attribute)) {
      return delegate.attr(attribute);
    } else {
      throw new NoSuchAttributeException(format("Attribute [%s] not found.", attribute));
    }
  }

  public final <E extends ImmutableElement> ImmutableList<E> select(
    @NonNull final String selector,
    @NonNull final Function<Element, E> toImmutableElement) {

    return delegate
      .select(selector).stream()
      .map(toImmutableElement)
      .collect(toImmutableList());
  }
}
