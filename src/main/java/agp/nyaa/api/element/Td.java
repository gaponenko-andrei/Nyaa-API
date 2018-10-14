package agp.nyaa.api.element;

import static com.google.common.base.Preconditions.checkArgument;

import org.jsoup.nodes.Element;

@javax.annotation.concurrent.Immutable
public final class Td extends ImmutableElement<Element> {

  public Td(final Element sourceElement) {
    super(validated(sourceElement));
  }

  private static Element validated(final Element sourceElement) {
    checkArgument(sourceElement.is("td"), "Provided element is not <td>.");
    return sourceElement;
  }
}
