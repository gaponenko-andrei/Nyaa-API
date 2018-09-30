package agp.nyaa.api.parsing;

import com.google.common.primitives.UnsignedInteger;
import lombok.NonNull;
import lombok.val;
import org.immutables.value.Value;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;
import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;

@Value.Immutable(builder = false)
@Value.Style(visibility = PACKAGE)
abstract class Tr {

  @Value.Parameter
  public abstract Element element();


  public static Tr of(@NonNull final Element element) {
    validate(element);
    return ImmutableTr.of(element);
  }

  private static void validate(final Element element) {
    checkArgument(element.is("tr"), "Provided element is not <tr>.");
  }

  public Td td(final int index) {
    return td(UnsignedInteger.valueOf(index));
  }

  public Td td(@NonNull final UnsignedInteger index) {
    val intIndex = index.intValue();
    val tdElement = this.element().select("td").get(intIndex);
    return Td.of(tdElement);
  }

  public String cssClass() {
    return Attributes.get(this.element(), "class");
  }
}
