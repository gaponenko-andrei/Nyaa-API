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
  public void constructorShouldNotAcceptNullArgument() {
    new ImmutableElement<>((Element) null);
  }

  @Test
  public void sourceElementShouldBeClonedDuringConstruction() {

    // Given
    val sourceElement = spy(newTestElement());

    // When
    new ImmutableElement<>(sourceElement);

    // Then
    verify(sourceElement).clone();
  }

  @Test
  public void sourceElementAndDelegateShouldNotHaveSameReference()
    throws NoSuchFieldException, IllegalAccessException {

    // Given
    val sourceElement = newTestElement();

    // When
    val delegate = getDelegateOf(new ImmutableElement<>(sourceElement));

    // Then
    assertNotSame(sourceElement, delegate);
  }

  @Test(dataProvider = "testUrlProvider")
  public void uriMethodShouldReturnBaseUriOfSourceElement(final String testUrl)
    throws URISyntaxException {

    // Given
    val sourceElement = newTestElementWithUri(testUrl);

    // When
    val actualUri = new ImmutableElement<>(sourceElement).uri();

    // Then
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

  @Test(expectedExceptions = NoSuchAttributeException.class)
  public void attrMethodShouldThrowWhenSourceElementDidntHaveRequestedAttribute() {

    // Given
    val sourceElementWithoutAttr = newTestElement();

    // When
    new ImmutableElement<>(sourceElementWithoutAttr).attr("class");
  }

  @Test
  public void attrMethodShouldReturnAttributeValueOfSourceElement() {

    // Given
    val sourceElementWithAttr = newTestElement().attr("class", "some-value");

    // When
    val attrValue = new ImmutableElement<>(sourceElementWithAttr).attr("class");

    // Then
    assertEquals(attrValue, "some-value");
  }

  /* hasAttr */

  @Test
  public void hasAttrMethodShouldReturnTrueWhenSourceElementHadIt() {

    // Given
    val sourceElementWithAttr = newTestElement().attr("class", "some-value");

    // When
    val result = new ImmutableElement<>(sourceElementWithAttr).hasAttr("class");

    // Then
    assertTrue(result);
  }

  @Test
  public void hasAttrMethodShouldReturnFalseWhenSourceElementDidntHaveIt() {

    // Given
    val sourceElementWithoutAttr = newTestElement();

    // When
    val result = new ImmutableElement<>(sourceElementWithoutAttr).hasAttr("class");

    // Then
    assertFalse(result);
  }

  /* text */

  @Test
  public void textMethodShouldReturnEmptyStringWhenSourceElementWasWithoutText() {

    // Given
    val sourceElementWithoutText = newTestElement();

    // When
    val actualText = new ImmutableElement<>(sourceElementWithoutText).text();

    // Then
    assertEquals(actualText, "");
  }

  @Test
  public void textMethodShouldReturnTextOfSourceElement() {

    // Given
    val sourceElementWithText = newTestElement().text("value");

    // When
    val actualText = new ImmutableElement<>(sourceElementWithText).text();

    // Then
    assertEquals(actualText, "value");
  }

  /* hasClass */

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void hasClassMethodShouldNotAcceptNullArguments() {

    // Given
    val sourceElementWithCssClass = newTestElement().attr("class", "value");

    // When
    new ImmutableElement<>(sourceElementWithCssClass).hasClass(null);
  }

  @Test
  public void hasClassMethodShouldReturnTrueForElementWithSpecifiedCssClass() {

    // Given
    val sourceElementWithCssClass = newTestElement().attr("class", "value");

    // When
    val result = new ImmutableElement<>(sourceElementWithCssClass).hasClass("value");

    // Then
    assertTrue(result);
  }

  @Test
  public void hasClassMethodShouldReturnFalseForElementWithoutSpecifiedCssClass() {

    // Given
    val sourceElementWithCssClass = newTestElement().attr("class", "value");

    // When
    val result = new ImmutableElement<>(sourceElementWithCssClass).hasClass("absent");

    // Then
    assertFalse(result);
  }

  /* Utility methods */

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
