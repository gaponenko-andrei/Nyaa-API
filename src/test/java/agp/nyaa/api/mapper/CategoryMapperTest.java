package agp.nyaa.api.mapper;

import agp.nyaa.api.TestCases;
import agp.nyaa.api.model.Category;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class CategoryMapperTest {

  private static final ImmutableMap<String, Category> MAPPING =
          ImmutableMap.<String, Category>builder()
                  .put("/?c=1_1", Category.Anime.MusicVideo.getInstance())
                  .put("/?c=1_2", Category.Anime.EnglishTranslated.getInstance())
                  .put("/?c=1_3", Category.Anime.NonEnglishTranslated.getInstance())
                  .put("/?c=1_4", Category.Anime.NonTranslated.getInstance())
                  .build();

  private final CategoryMapper<String, Category> mapper = CategoryMapper.fromHref();


  @Test(dataProvider = "hrefsProvider")
  public void fromHrefMapping(final String href) {

    /* Act */
    val category = mapper.map(href);

    /* Assert */
    assertEquals(category, getCategoryBy(href));
  }

  @Test
  void fromHrefMapperThrowsOnNullArgument() {
    assertThrows(NullPointerException.class, () -> mapper.map(null));
  }

  @Test
  void fromHrefMapperThrowsOnUnknownArgument() {
    assertThrows(IllegalArgumentException.class, () -> mapper.map("unknown_href"));
  }

  @DataProvider(name = "hrefsProvider")
  private static Iterator<Object[]> getTestHrefs() {
    return TestCases.from(MAPPING.keySet());
  }

  private static Category getCategoryBy(final String href) {
    return MAPPING.get(href);
  }
}
