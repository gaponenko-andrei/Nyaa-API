package agp.nyaa.api.element;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.testng.annotations.Test;

public class TdTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorShouldThrowOnNonTdElements() {
    new Td(newNonTdElement());
  }

  private Element newNonTdElement() {
    return new Element(Tag.valueOf("table"), "baseUri");
  }

  @Test
  public void constructorShouldAcceptTdElements() {
    new Td(newTdElement());
  }

  private Element newTdElement() {
    return new Element(Tag.valueOf("td"), "baseUri");
  }
}
