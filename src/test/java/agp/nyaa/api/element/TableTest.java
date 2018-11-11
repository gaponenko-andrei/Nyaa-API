package agp.nyaa.api.element;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.testng.annotations.Test;

public class TableTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorShouldThrowOnNonTableElements() {
    new Table(newNonTableElement());
  }

  private Element newNonTableElement() {
    return new Element(Tag.valueOf("tr"), "baseUrl");
  }

  @Test
  public void constructorShouldAcceptTableElements() {
    new Table(newTableElement());
  }

  private Element newTableElement() {
    return new Element(Tag.valueOf("table"), "baseUri");
  }
}
