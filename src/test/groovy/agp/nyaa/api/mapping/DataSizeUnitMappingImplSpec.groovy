package agp.nyaa.api.mapping

import agp.nyaa.api.model.DataSize
import com.google.common.collect.ImmutableMap
import spock.lang.Specification


class DataSizeUnitMappingImplSpec extends Specification {

  static final ImmutableMap<String, DataSize.Unit> TESTED_VALUES =
    ImmutableMap.<String, DataSize.Unit> builder()
      .put("Bytes", DataSize.Unit.BYTE)
      .put("KiB", DataSize.Unit.KILOBYTE)
      .put("MiB", DataSize.Unit.MEGABYTE)
      .put("GiB", DataSize.Unit.GIGABYTE)
      .put("TiB", DataSize.Unit.TERABYTE)
      .build()

  DataSizeUnitMapping mapping = DataSizeUnitMapping.impl()


  def "Supported values should match tested values"() {
    expect:
      mapping.supportedValues() == TESTED_VALUES.keySet()
  }

  def "Mapping should throw on null"() {

    when:
      mapping.apply(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Mapping should throw on unsupported unit string"() {

    when:
      mapping.apply(unsupportedUnitString)

    then:
      thrown(IllegalArgumentException)

    where:
      unsupportedUnitString << ["", " ", "PiB", "unsupported"]
  }

  def "Mapping supported unit string should produce expected result"() {

    when:
      final DataSize.Unit result = mapping.apply(supportedUnitString)

    then:
      result == getExpectedUnitBy(supportedUnitString)

    where:
      supportedUnitString << TESTED_VALUES.keySet()
  }

  DataSize.Unit getExpectedUnitBy(final String unitString) {
    TESTED_VALUES.get(unitString)
  }
}