package agp.nyaa.api.mapping

import agp.nyaa.api.model.DataSize
import agp.nyaa.api.test.TestDataSizeUnitMapping
import spock.lang.Specification

import static agp.nyaa.api.model.DataSize.Unit.BYTE
import static java.util.Collections.emptyMap


class DataSizeMappingSpec extends Specification {

  final DataSizeUnitMapping testUnitMapping = new TestDataSizeUnitMapping().from("TestUnit").to(BYTE)


  def "Mapping of valid data size string should produce expected result"() {

    given:
      def mapping = DataSizeMapping.using(testUnitMapping)

    when:
      final DataSize result = mapping.apply(dataSizeString)

    then:
      result == expectedResult

    where:
      dataSizeString         | expectedResult
      "1 TestUnit"           | DataSize.of(1, BYTE)
      "1.0 TestUnit"         | DataSize.of(1.0f, BYTE)
      "20.20 TestUnit"       | DataSize.of(20.20f, BYTE)
      " 300  TestUnit  "     | DataSize.of(300, BYTE)
      " 300.300  TestUnit  " | DataSize.of(300.300f, BYTE)
  }

  def "Mapping of unsupported or invalid data size string should throw"() {

    given:
      def mapping = DataSizeMapping.using(testUnitMapping)

    when:
      mapping.apply(dataSizeString)

    then:
      thrown(IllegalArgumentException)

    where:
      dataSizeString << [
        "1 Unsupported",
        "0x1 TestUnit",
        "0 TestUnit",
        "0.0 TestUnit",
        "-10 TestUnit",
        "-10.0 TestUnit"
      ]
  }

  def "Provided unit mapping should be used"() {

    given:
      def mapping = DataSizeMapping.using(Spy(testUnitMapping))

    when:
      mapping.apply("100 TestUnit")

    then:
      1 * mapping.getUnitMapping().apply("TestUnit")
  }

  def "Using null unit mapping should throw"() {

    when:
      DataSizeMapping.using(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Using unit mapping without supported units should throw"() {

    given:
      def unitMapping = DataSizeUnitMapping.from(emptyMap())

    when:
      DataSizeMapping.using(unitMapping)

    then:
      thrown(IllegalArgumentException)

    and:
      unitMapping.supportedValues().size() == 0
  }
}