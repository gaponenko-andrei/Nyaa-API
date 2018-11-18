package agp.nyaa.api.mapper;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import agp.nyaa.api.model.Category;
import agp.nyaa.api.test.TestCases;

public class CategoryMapperTest {

  private static final ImmutableMap<String, Category> MAPPING =
    ImmutableMap.<String, Category>builder()
      .put("/?c=1_1", Category.Anime.MusicVideo.instance())
      .put("/?c=1_2", Category.Anime.EnglishTranslated.instance())
      .put("/?c=1_3", Category.Anime.NonEnglishTranslated.instance())
      .put("/?c=1_4", Category.Anime.NonTranslated.instance())
      .build();

  private CategoryMapper mapper = new CategoryMapper();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mapperShouldThrowOnNulls() {
    mapper.map(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mapperShouldThrowOnUnsupportedCategoryHrefs() {
    mapper.map("unsupported");
  }

  @Test
  public void supportedValuesShouldMatchTestedValues() {
    assertEquals(mapper.supportedValues(), MAPPING.keySet());
  }

  @Test(dataProvider = "categoryHrefTestCasesProvider")
  public void mappingSupportedCategoryHrefShouldProduceExpectedResult(final String categoryHref) {

    // given
    final Category actualMappingResult = mapper.map(categoryHref);

    // Expect
    assertEquals(actualMappingResult, getExpectedCategoryBy(categoryHref));
  }

  @DataProvider(name = "categoryHrefTestCasesProvider")
  private static Iterator<Object[]> getCategoryHrefTestCases() {
    return TestCases.from(MAPPING.keySet());
  }

  private static Category getExpectedCategoryBy(final String categoryHref) {
    return MAPPING.get(categoryHref);
  }
}
