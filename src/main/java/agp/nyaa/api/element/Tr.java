package agp.nyaa.api.element;

import static com.google.common.base.Preconditions.checkArgument;

import org.jsoup.nodes.Element;

@javax.annotation.concurrent.Immutable
public final class Tr extends ImmutableElement<Element> {

  public Tr(final Element sourceElement) {
    super(validated(sourceElement));
  }

  private static Element validated(final Element sourceElement) {
    checkArgument(sourceElement.is("tr"), "Provided element is not <tr>.");
    return sourceElement;
  }
}
