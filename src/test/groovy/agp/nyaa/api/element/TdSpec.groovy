package agp.nyaa.api.element

import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import spock.lang.Specification


class TdSpec extends Specification {

  def "Constructor should accept <td> element"() {

    given:
      final Element tdElement = new Element(Tag.valueOf("td"), "baseUrl")

    when:
      new Td(tdElement)

    then:
      notThrown(Exception)
  }

  def "Constructor should throw on non <td> element"() {

    given:
      final Element nonTdElement = new Element(Tag.valueOf("tr"), "baseUrl")

    when:
      new Td(nonTdElement)

    then:
      thrown(IllegalArgumentException)
  }
}