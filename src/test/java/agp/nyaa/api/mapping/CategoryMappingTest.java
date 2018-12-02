package agp.nyaa.api.mapping;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import agp.nyaa.api.model.Category;
import agp.nyaa.api.test.TestCases;

public class CategoryMappingTest {

  private static final ImmutableMap<String, Category> TESTED_VALUES =
    ImmutableMap.<String, Category>builder()
      .put("/?c=1_1", Category.Anime.MusicVideo.instance())
      .put("/?c=1_2", Category.Anime.EnglishTranslated.instance())
      .put("/?c=1_3", Category.Anime.NonEnglishTranslated.instance())
      .put("/?c=1_4", Category.Anime.NonTranslated.instance())
      .build();

  private CategoryMapping mapping = new CategoryMapping();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnNull() {
    mapping.apply(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnUnsupportedCategoryHref() {
    mapping.apply("unsupported");
  }

  @Test
  public void supportedValuesShouldMatchTestedValues() {
    assertEquals(mapping.supportedValues(), TESTED_VALUES.keySet());
  }

  @Test(dataProvider = "categoryHrefTestCasesProvider")
  public void mappingSupportedCategoryHrefShouldProduceExpectedResult(final String categoryHref) {

    // given
    final Category actualMappingResult = mapping.apply(categoryHref);

    // expect
    assertEquals(actualMappingResult, getExpectedCategoryBy(categoryHref));
  }

  @DataProvider(name = "categoryHrefTestCasesProvider")
  private static Iterator<Object[]> getCategoryHrefTestCases() {
    return TestCases.from(TESTED_VALUES.keySet());
  }

  private static Category getExpectedCategoryBy(final String categoryHref) {
    return TESTED_VALUES.get(categoryHref);
  }
}
