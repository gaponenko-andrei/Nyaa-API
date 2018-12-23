package agp.nyaa.api.element

import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import spock.lang.Specification


class TableSpec extends Specification {

  def "Constructor should accept <table> element"() {

    given:
      final Element tableElement = new Element(Tag.valueOf("table"), "baseUrl")

    when:
      new Table(tableElement)

    then:
      notThrown(Exception)
  }

  def "Constructor should throw on non <table> element"() {

    given:
      final Element nonTableElement = new Element(Tag.valueOf("tr"), "baseUrl")

    when:
      new Table(nonTableElement)

    then:
      thrown(IllegalArgumentException)
  }
}