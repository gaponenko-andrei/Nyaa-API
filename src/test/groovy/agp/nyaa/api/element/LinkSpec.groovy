package agp.nyaa.api.element

import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import spock.lang.Specification


class LinkSpec extends Specification {

  def "Constructor should accept <a> element"() {

    given:
      final Element linkElement = new Element(Tag.valueOf("a"), "baseUrl")

    when:
      new Link(linkElement)

    then:
      notThrown(Exception)
  }

  def "Constructor should throw on non <a> element"() {

    given:
      final Element nonLinkElement = new Element(Tag.valueOf("tr"), "baseUrl")

    when:
      new Link(nonLinkElement)

    then:
      thrown(IllegalArgumentException)
  }
}