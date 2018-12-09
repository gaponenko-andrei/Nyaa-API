package agp.nyaa.api.model

import spock.lang.Specification

import static agp.nyaa.api.model.DataSize.Unit.BYTE


class DataSizeSpec extends Specification {

  def "Constructor should throw on null integer values"() {

    when:
      DataSize.of((Integer) null, BYTE)

    then:
      thrown(IllegalArgumentException)
  }

  def "Constructor should throw on null float values"() {

    when:
      DataSize.of((Float) null, BYTE)

    then:
      thrown(IllegalArgumentException)
  }

  def "Constructor should throw on null unit"() {

    when:
      DataSize.of(1, null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Integer DataSize should equal float DataSize with zero fraction value"() {

    expect:
      DataSize.of(1, unit) == DataSize.of(1f, unit)

    and:
      DataSize.of(1, unit) == DataSize.of(1.0f, unit)

    where:
      unit << DataSize.Unit.values()
  }

}