package agp.nyaa.api.element;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import agp.nyaa.api.exception.NoSuchAttributeException;
import agp.nyaa.api.test.TestCases;
import lombok.val;

public class ImmutableElementTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorShouldThrowOnNull() {
    new ImmutableElement<>((Element) null);
  }

  @Test
  public void sourceElementShouldBeClonedDuringConstruction() {

    // given
    val sourceElement = spy(newTestElement());

    // when
    new ImmutableElement<>(sourceElement);

    // then
    verify(sourceElement).clone();
  }

  @Test
  public void sourceElementAndDelegateShouldNotHaveSameReference()
    throws NoSuchFieldException, IllegalAccessException {

    // given
    val sourceElement = newTestElement();

    // when
    val delegate = getDelegateOf(new ImmutableElement<>(sourceElement));

    // then
    assertNotSame(sourceElement, delegate);
  }

  @Test(dataProvider = "testUrlProvider")
  public void uriMethodShouldReturnBaseUriOfSourceElement(final String testUrl)
    throws URISyntaxException {

    // given
    val sourceElement = newTestElementWithUri(testUrl);

    // when
    val actualUri = new ImmutableElement<>(sourceElement).uri();

    // then
    assertEquals(actualUri, new URI(testUrl));
  }

  @DataProvider(name = "testUrlProvider")
  private static Iterator<Object[]> getTestUri() {
    return TestCases.forEach(
      "http://nyaa.si"
      , "http://nyaa.si/"
      , "https://nyaa.si/"
      , "https://nyaa.123unblock.party/?f=0&c=1_0&q=Full+Metal+Panic%21+Invisible+Victory"
    );
  }

  /* attr */

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void attrMethodShouldThrowOnNull() {

    // given
    val sourceElementWithoutAttr = newTestElement();

    // when
    new ImmutableElement<>(sourceElementWithoutAttr).attr(null);
  }

  @Test(expectedExceptions = NoSuchAttributeException.class)
  public void attrMethodShouldThrowWhenSourceElementDidntHaveRequestedAttribute() {

    // given
    val sourceElementWithoutAttr = newTestElement();

    // when
    new ImmutableElement<>(sourceElementWithoutAttr).attr("class");
  }

  @Test
  public void attrMethodShouldReturnAttributeValueOfSourceElement() {

    // given
    val sourceElementWithAttr = newTestElement().attr("class", "some-value");

    // when
    val attrValue = new ImmutableElement<>(sourceElementWithAttr).attr("class");

    // then
    assertEquals(attrValue, "some-value");
  }

  /* hasClass */

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void hasClassMethodShouldThrowOnNull() {

    // given
    val sourceElementWithCssClass = newTestElement().attr("class", "value");

    // when
    new ImmutableElement<>(sourceElementWithCssClass).hasClass(null);
  }

  @Test
  public void hasClassMethodShouldReturnTrueForElementWithSpecifiedCssClass() {

    // given
    val sourceElementWithCssClass = newTestElement().attr("class", "value");

    // when
    val result = new ImmutableElement<>(sourceElementWithCssClass).hasClass("value");

    // then
    assertTrue(result);
  }

  @Test
  public void hasClassMethodShouldReturnFalseForElementWithoutSpecifiedCssClass() {

    // given
    val sourceElementWithCssClass = newTestElement().attr("class", "value");

    // when
    val result = new ImmutableElement<>(sourceElementWithCssClass).hasClass("absent");

    // then
    assertFalse(result);
  }

  /* has */

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void hasMethodShouldThrowOnNull() {

    // given
    val sourceElementWithChild = newTestElement().append("<div><i class='x'/></div>");

    // when
    new ImmutableElement<>(sourceElementWithChild).has(null);
  }

  @Test(description =
    "Method 'has' should return 'true' for provided selector when " +
      "source element actually has something matching this selector"
  )
  public void hasMethodShouldReturnTrueWhenExpected() {

    // given
    val sourceElementWithChild = newTestElement().append("<div><i class='x'/></div>");

    // when
    val result = new ImmutableElement<>(sourceElementWithChild).has("i.x");

    // then
    assertTrue(result);
  }

  @Test(description =
    "Method 'has' should return 'false' for provided selector " +
      "when source element has nothing matching this selector"
  )
  public void hasMethodShouldReturnFalseWhenExpected() {

    // given
    val sourceElementWithChild = newTestElement().append("<div><i class='x'/></div>");

    // when
    val result = new ImmutableElement<>(sourceElementWithChild).has("i.y");

    // then
    assertFalse(result);
  }

  /* hasAttr */

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void hasAttrMethodShouldThrowOnNull() {

    // given
    val sourceElementWithAttr = newTestElement().attr("class", "some-value");

    // when
    new ImmutableElement<>(sourceElementWithAttr).hasAttr(null);
  }

  @Test
  public void hasAttrMethodShouldReturnTrueWhenSourceElementHadIt() {

    // given
    val sourceElementWithAttr = newTestElement().attr("class", "some-value");

    // when
    val result = new ImmutableElement<>(sourceElementWithAttr).hasAttr("class");

    // then
    assertTrue(result);
  }

  @Test
  public void hasAttrMethodShouldReturnFalseWhenSourceElementDidntHaveIt() {

    // given
    val sourceElementWithoutAttr = newTestElement();

    // when
    val result = new ImmutableElement<>(sourceElementWithoutAttr).hasAttr("class");

    // then
    assertFalse(result);
  }

  /* text */

  @Test
  public void textMethodShouldReturnEmptyStringWhenSourceElementWasWithoutText() {

    // given
    val sourceElementWithoutText = newTestElement();

    // when
    val actualText = new ImmutableElement<>(sourceElementWithoutText).text();

    // then
    assertEquals(actualText, "");
  }

  @Test
  public void textMethodShouldReturnTextOfSourceElement() {

    // given
    val sourceElementWithText = newTestElement().text("value");

    // when
    val actualText = new ImmutableElement<>(sourceElementWithText).text();

    // then
    assertEquals(actualText, "value");
  }

  /* utils */

  private Element newTestElement() {
    return new Element("tag");
  }

  private Element newTestElementWithUri(final String baseUrl) {
    return new Element(Tag.valueOf("table"), baseUrl);
  }

  private Element getDelegateOf(final ImmutableElement immutableElement)
    throws NoSuchFieldException, IllegalAccessException {

    val delegateField = ImmutableElement.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    return (Element) delegateField.get(immutableElement);
  }
}
