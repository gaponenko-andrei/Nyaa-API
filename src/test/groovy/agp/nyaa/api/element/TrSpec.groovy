package agp.nyaa.api.element

import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import spock.lang.Specification


class TrSpec extends Specification {

  def "Constructor should accept <tr> element"() {

    given:
      final Element trElement = new Element(Tag.valueOf("tr"), "baseUrl")

    when:
      new Tr(trElement)

    then:
      notThrown(Exception)
  }

  def "Constructor should throw on non <tr> element"() {

    given:
      final Element nonTrElement = new Element(Tag.valueOf("td"), "baseUrl")

    when:
      new Tr(nonTrElement)

    then:
      thrown(IllegalArgumentException)
  }
}