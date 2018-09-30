package agp.nyaa.api.parsing;

import lombok.NonNull;
import org.immutables.value.Value;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;
import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;

@Value.Immutable(builder = false)
@Value.Style(visibility = PACKAGE)
abstract class Link {

  @Value.Parameter
  public abstract Element element();


  public static Link of(@NonNull final Element element) {
    validate(element);
    return ImmutableLink.of(element);
  }

  private static void validate(final Element element) {
    checkArgument(element.is("a"), "Provided element is not <a>.");
  }

  public String href() {
    return Attributes.get(this.element(), "href");
  }

  public String title() {
    return Attributes.get(this.element(), "title");
  }
}
