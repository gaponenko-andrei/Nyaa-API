package agp.nyaa.api.element

import agp.nyaa.api.exception.NoSuchAttributeException
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import spock.lang.Specification

class ImmutableElementSpec extends Specification {

  def "Constructor should throw on null"() {

    when:
      new ImmutableElement(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Source element should be cloned during construction"() {

    given:
      final Element sourceElement = Spy(newTestElement())

    when:
      new ImmutableElement(sourceElement)

    then:
      1 * sourceElement.clone()
  }

  def "Source element and delegate are not same"() {

    given:
      final Element sourceElement = newTestElement()

    and:
      final ImmutableElement immutableElement = new ImmutableElement(sourceElement)

    when:
      final Element delegate = getDelegateOf(immutableElement)

    then:
      !delegate.is(sourceElement)
  }

  def "Method uri() should return base uri of source element"() {

    given:
      final Element sourceElement = newTestElementWithUri(testUriString)

    when:
      final URI actualUri = new ImmutableElement(sourceElement).uri()

    then:
      actualUri == new URI(testUriString)

    where:
      testUriString << [
        "http://nyaa.si",
        "http://nyaa.si/",
        "https://nyaa.si/",
        "https://nyaa.123unblock.party/?f=0&c=1_0&q=Full+Metal+Panic%21+Invisible+Victory"
      ]
  }

  /* attr */

  def "Method attr() should throw on null"() {

    when:
      new ImmutableElement(newTestElement()).attr(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Method attr() should throw when source element doesn't have requested attribute"() {

    given:
      final Element sourceElementWithoutAttr = newTestElement()

    when:
      new ImmutableElement(sourceElementWithoutAttr).attr("class")

    then:
      thrown(NoSuchAttributeException)
  }

  def "Method attr() should return attribute value of source element"() {

    given:
      final Element sourceElement = newTestElement().attr("class", "some-value")

    when:
      final String attrValue = new ImmutableElement(sourceElement).attr("class")

    then:
      attrValue == "some-value"
  }

  /* hasClass */

  def "Method hasClass() should throw on null"() {

    when:
      new ImmutableElement(newTestElement()).hasClass(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Method hasClass() should return true for method with specified css class"() {

    given:
      final Element sourceElementWithClass = newTestElement().addClass("value")

    expect:
      new ImmutableElement(sourceElementWithClass).hasClass("value")
  }

  def "Method hasClass() should return false for method without specified css class"() {

    given:
      final Element sourceElementWithClass = newTestElement().addClass("value")

    expect:
      !new ImmutableElement(sourceElementWithClass).hasClass("anotherValue")
  }

  /* has */

  def "Method has() should throw on null"() {

    when:
      new ImmutableElement(newTestElement()).has(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Method has() should return 'true' for provided selector when source element had a match"() {

    given:
      final Element sourceElementWithMatch = newTestElement().append("<div><i class='x'/></div>")

    expect:
      new ImmutableElement(sourceElementWithMatch).has("i.x")
  }

  def "Method has() should return 'false' for provided selector when source element had no match"() {

    given:
      final Element sourceElementWithMatch = newTestElement().append("<div><i class='x'/></div>")

    expect:
      !new ImmutableElement(sourceElementWithMatch).has("i.y")
  }

  /* hasAttr */

  def "Method hasAttr() should throw on null"() {

    when:
      new ImmutableElement(newTestElement()).hasAttr(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Method hasAttr() should return 'true' when source element had it"() {

    given:
      final Element sourceElementWithAttr = newTestElement().attr("class", "some-value")

    expect:
      new ImmutableElement(sourceElementWithAttr).hasAttr("class")
  }

  def "Method hasAttr() should return 'false' when source element didn't have it"() {

    given:
      final Element sourceElementWithAttr = newTestElement().attr("class", "some-value")

    expect:
      !new ImmutableElement(sourceElementWithAttr).hasAttr("missing-attr")
  }

  /* text */

  def "Method text() should return empty string when source element was without text"() {

    given:
      final Element sourceElementWithoutText = newTestElement()

    expect:
      new ImmutableElement(sourceElementWithoutText).text().isEmpty()
  }

  def "Method text() should return text of source element"() {

    given:
      final Element sourceElementWithText = newTestElement().text("some text")

    expect:
      new ImmutableElement(sourceElementWithText).text() == "some text"
  }

  /* utils */

  Element newTestElement() {
    new Element("tag");
  }

  Element newTestElementWithUri(final String baseUrl) {
    new Element(Tag.valueOf("table"), baseUrl);
  }

  Element getDelegateOf(final ImmutableElement immutableElement) {
    def delegateField = ImmutableElement.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.get(immutableElement) as Element;
  }
}