package agp.nyaa.api.mapping

import agp.nyaa.api.model.Category
import com.google.common.collect.ImmutableMap
import spock.lang.Specification


class CategoryMappingSpec extends Specification {

  static final ImmutableMap<String, Category> TESTED_VALUES =
    ImmutableMap.<String, Category> builder()
      .put("/?c=1_1", Category.Anime.MusicVideo.instance())
      .put("/?c=1_2", Category.Anime.EnglishTranslated.instance())
      .put("/?c=1_3", Category.Anime.NonEnglishTranslated.instance())
      .put("/?c=1_4", Category.Anime.NonTranslated.instance())
      .build()

  CategoryMapping mapping = new CategoryMapping()


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

  def "Mapping should throw on unsupported category href"() {

    when:
      mapping.apply(unsupportedCategoryHref)

    then:
      thrown(IllegalArgumentException)

    where:
      unsupportedCategoryHref << ["", " ", "/?c=1_5", "unsupported"]
  }

  def "Mapping supported category href should produce expected result"() {

    when:
      final Category result = mapping.apply(supportedCategoryHref)

    then:
      result == getExpectedCategoryBy(supportedCategoryHref)

    where:
      supportedCategoryHref << TESTED_VALUES.keySet()
  }

  Category getExpectedCategoryBy(final String categoryHref) {
    TESTED_VALUES.get(categoryHref)
  }
}