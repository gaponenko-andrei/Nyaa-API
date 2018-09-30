package agp.nyaa.api.parsing;

import agp.nyaa.api.exception.parse.ParseException;
import com.google.common.primitives.UnsignedInteger;
import lombok.NonNull;
import lombok.val;
import org.immutables.value.Value;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;
import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;

@Value.Immutable(builder = false)
@Value.Style(visibility = PACKAGE)
abstract class Td {

  @Value.Parameter
  public abstract Element element();


  public static Td of(@NonNull final Element element) {
    validate(element);
    return ImmutableTd.of(element);
  }

  private static void validate(final Element element) {
    checkArgument(element.is("td"), "Provided element is not <td>.");
  }

  public Link link(final int index) {
    return link(UnsignedInteger.valueOf(index));
  }

  public Link link(@NonNull final UnsignedInteger index) {
    val intIndex = index.intValue();
    val linkElement = this.element().select("a").get(intIndex);
    return Link.of(linkElement);
  }

  public String text() {
    if (this.element().hasText()) {
      return this.element().text();
    } else {
      throw new ParseException("Element has no text.");
    }
  }
}
