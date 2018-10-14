package agp.nyaa.api.element;

import static com.google.common.base.Preconditions.checkArgument;

import org.jsoup.nodes.Element;

@javax.annotation.concurrent.Immutable
public final class Link extends ImmutableElement<Element> {

  public Link(final Element sourceElement) {
    super(validated(sourceElement));
  }

  private static Element validated(final Element sourceElement) {
    checkArgument(sourceElement.is("a"), "Provided element is not <a>.");
    return sourceElement;
  }
}
