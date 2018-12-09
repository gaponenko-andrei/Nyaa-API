package agp.nyaa.api.element;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.testng.annotations.Test;

public class TrTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorShouldThrowOnNonTrElements() {
    new Tr(newNonTrElement());
  }

  private Element newNonTrElement() {
    return new Element(Tag.valueOf("table"), "baseUri");
  }

  @Test
  public void constructorShouldAcceptTrElements() {
    new Tr(newTrElement());
  }

  private Element newTrElement() {
    return new Element(Tag.valueOf("tr"), "baseUri");
  }
}