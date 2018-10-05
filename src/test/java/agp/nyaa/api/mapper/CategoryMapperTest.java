package agp.nyaa.api.mapper;

import agp.nyaa.api.model.Category;
import agp.nyaa.api.test.TestCases;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;

public class CategoryMapperTest {

  private static final ImmutableMap<String, Category> MAPPING =
    ImmutableMap.<String, Category>builder()
      .put("/?c=1_1", Category.Anime.MusicVideo.instance())
      .put("/?c=1_2", Category.Anime.EnglishTranslated.instance())
      .put("/?c=1_3", Category.Anime.NonEnglishTranslated.instance())
      .put("/?c=1_4", Category.Anime.NonTranslated.instance())
      .build();

  private CategoryMapper mapper = new CategoryMapper();


  @Test(dataProvider = "categoryHrefTestCasesProvider")
  public void mapping(final String categoryHref) {

    /* Act */
    final Category actualMappingResult = mapper.map(categoryHref);

    /* Assert */
    assertEquals(actualMappingResult, getExpectedCategoryBy(categoryHref));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNullCategoryHref() {
    mapper.map(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnUnknownCategoryHref() {
    mapper.map("unknown");
  }

  @Test
  public void supportedValues() {
    assertEquals(mapper.supportedValues(), MAPPING.keySet());
  }

  @DataProvider(name = "categoryHrefTestCasesProvider")
  private static Iterator<Object[]> getCategoryHrefTestCases() {
    return TestCases.from(MAPPING.keySet());
  }

  private static Category getExpectedCategoryBy(final String categoryHref) {
    return MAPPING.get(categoryHref);
  }
}
