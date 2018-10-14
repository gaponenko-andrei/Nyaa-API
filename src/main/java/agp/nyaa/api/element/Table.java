package agp.nyaa.api.element;

import static com.google.common.base.Preconditions.checkArgument;

import org.jsoup.nodes.Element;

@javax.annotation.concurrent.Immutable
public final class Table extends ImmutableElement<Element> {

  public Table(final Element sourceElement) {
    super(validated(sourceElement));
  }

  private static Element validated(final Element sourceElement) {
    checkArgument(sourceElement.is("table"), "Provided element is not <table>.");
    return sourceElement;
  }
}
