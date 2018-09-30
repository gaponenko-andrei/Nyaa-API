package agp.nyaa.api.parsing;

import agp.nyaa.api.exception.parse.AbsentAttributeException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jsoup.nodes.Element;

@UtilityClass
class Attributes {

  public static String get(@NonNull final Element owner, @NonNull final String attribute) {
    if (owner.hasAttr(attribute)) {
      return owner.attr(attribute);
    } else {
      throw new AbsentAttributeException(String.format("Attribute '%s' not found.", attribute));
    }
  }
}
