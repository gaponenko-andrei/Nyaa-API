package agp.nyaa.api.mapping

import spock.lang.Specification


class StringToUriMappingSpec extends Specification {

  StringToUriMapping mapping = new StringToUriMapping()


  def "Mapping should throw on null"() {

    when:
      mapping.apply(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Mapping should throw on invalid uri string"() {

    when:
      mapping.apply("\\some_invalid_link\\")

    then:
      thrown(IllegalArgumentException)
  }

  def "Mapping valid uri string should produce uri with expected path"() {

    given:
      def validUriString = "/download/1032497.torrent"

    when:
      final URI result = mapping.apply(validUriString)

    then:
      result.path == validUriString
  }
}