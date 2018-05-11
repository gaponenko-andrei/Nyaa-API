package agp.nyaa.api.mapper;

import agp.nyaa.api.test.TestCases;
import agp.nyaa.api.model.Category;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CategoryMapperTest {

  private static final ImmutableMap<String, Category> MAPPING =
    ImmutableMap.<String, Category>builder()
      .put("/?c=1_1", Category.Anime.MusicVideo.getInstance())
      .put("/?c=1_2", Category.Anime.EnglishTranslated.getInstance())
      .put("/?c=1_3", Category.Anime.NonEnglishTranslated.getInstance())
      .put("/?c=1_4", Category.Anime.NonTranslated.getInstance())
      .build();

  private CategoryMapper mapper;
  private String categoryHref;
  private Category mappingResult;

  @BeforeMethod
  public void setUp() {
    mapper = new CategoryMapper();
  }

  @Test(dataProvider = "categoryHrefTestCasesProvider")
  public void mapping(final String categoryHref) {

    /* Arrange */
    givenCategoryHrefIs(categoryHref);

    /* Act */
    mapCategoryHref();

    /* Assert */
    assertExpectedResult();
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void throwsOnNullCategoryHref() {

    /* Arrange */
    givenCategoryHrefIs(null);

    /* Act */
    mapCategoryHref();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnUnknownCategoryHref() {

    /* Arrange */
    givenCategoryHrefIs("unknown");

    /* Act */
    mapCategoryHref();
  }

  @Test
  public void supportedValues() {
    assertEquals(mapper.supportedValues(), MAPPING.keySet());
  }

  private void givenCategoryHrefIs(final String categoryHref) {
    this.categoryHref = categoryHref;
  }

  private void mapCategoryHref() {
    this.mappingResult = mapper.map(categoryHref);
  }

  private void assertExpectedResult() {
    assertEquals(mappingResult, getExpectedCategoryBy(categoryHref));
  }

  @DataProvider(name = "categoryHrefTestCasesProvider")
  private static Iterator<Object[]> getCategoryHrefTestCases() {
    return TestCases.from(MAPPING.keySet());
  }

  private static Category getExpectedCategoryBy(final String categoryHref) {
    return MAPPING.get(categoryHref);
  }
}
