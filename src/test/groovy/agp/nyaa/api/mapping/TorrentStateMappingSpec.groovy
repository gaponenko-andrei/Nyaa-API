package agp.nyaa.api.mapping

import agp.nyaa.api.model.TorrentState
import com.google.common.collect.ImmutableMap
import spock.lang.Specification

import static agp.nyaa.api.model.TorrentState.*

class TorrentStateMappingSpec extends Specification {

  static final ImmutableMap<String, TorrentState> TESTED_VALUES =
    ImmutableMap.<String, TorrentState> builder()
      .put("default", NORMAL)
      .put("danger", REMAKE)
      .put("success", TRUSTED)
      .build()

  TorrentStateMapping mapping = new TorrentStateMapping()


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

  def "Mapping should throw on unsupported css class"() {

    when:
      mapping.apply(unsupportedCssClass)

    then:
      thrown(IllegalArgumentException)

    where:
      unsupportedCssClass << ["", " ", "normal", "unsupported"]
  }

  def "Mapping supported unit string should produce expected result"() {

    when:
      final TorrentState result = mapping.apply(supportedCssClass)

    then:
      result == getExpectedTorrentStateBy(supportedCssClass)

    where:
      supportedCssClass << TESTED_VALUES.keySet()
  }

  TorrentState getExpectedTorrentStateBy(final String unitString) {
    TESTED_VALUES.get(unitString)
  }
}