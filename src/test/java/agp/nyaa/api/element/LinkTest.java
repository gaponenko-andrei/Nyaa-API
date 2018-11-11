package agp.nyaa.api.element;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.testng.annotations.Test;

public class LinkTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorShouldThrowOnNonLinkElements() {
    new Link(newNonLinkElement());
  }

  private Element newNonLinkElement() {
    return new Element(Tag.valueOf("tr"), "baseUrl");
  }

  @Test
  public void constructorShouldAcceptLinkElements() {
    new Link(newLinkElement());
  }

  private Element newLinkElement() {
    return new Element(Tag.valueOf("a"), "baseUri");
  }
}
