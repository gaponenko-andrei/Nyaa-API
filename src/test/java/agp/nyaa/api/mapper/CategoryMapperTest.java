package agp.nyaa.api.mapper;

import agp.nyaa.api.model.Category;
import com.google.common.collect.ImmutableBiMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryMapperTest {

  private static final ImmutableBiMap<Category, String> MAPPING =
          ImmutableBiMap.<Category, String>
                  builder()
                  .put(Category.Anime.MusicVideo.getInstance(), "/?c=1_1")
                  .put(Category.Anime.EnglishTranslated.getInstance(), "/?c=1_2")
                  .put(Category.Anime.NonEnglishTranslated.getInstance(), "/?c=1_3")
                  .put(Category.Anime.NonTranslated.getInstance(), "/?c=1_4")
                  .build();

  //
  // Category -> Href
  //

  @ParameterizedTest
  @MethodSource("categoriesProvider")
  void toHrefMapping(final Category category) {

    /* Arrange */
    final Mapper<Category, String> toHrefMapper = CategoryMapper.toHref();

    /* Act */
    final String href = toHrefMapper.map(category);

    /* Assert */
    assertEquals(getHrefBy(category), href);
  }

  @Test
  void toHrefMapperThrowsOnNullArgument() {
    assertThrows(NullPointerException.class,
            () -> CategoryMapper.toHref().map(null));
  }

  @Test
  void toHrefMapperThrowsOnUnknownArgument() {
    assertThrows(IllegalArgumentException.class,
            () -> CategoryMapper.toHref().map(Category.Anime.getInstance()));
  }

  private static Stream<Category> categoriesProvider() {
    return MAPPING.keySet().stream();
  }

  private static String getHrefBy(final Category category) {
    return MAPPING.get(category);
  }

  //
  // Href -> Category
  //

  @ParameterizedTest
  @MethodSource("hrefsProvider")
  void fromHrefMapping(final String href) {

    /* Arrange */
    final Mapper<String, Category> fromHrefMapper = CategoryMapper.fromHref();

    /* Act */
    final Category category = fromHrefMapper.map(href);

    /* Assert */
    assertEquals(getCategoryBy(href), category);
  }

  @Test
  void fromHrefMapperThrowsOnNullArgument() {
    assertThrows(NullPointerException.class,
            () -> CategoryMapper.fromHref().map(null));
  }

  @Test
  void fromHrefMapperThrowsOnUnknownArgument() {
    assertThrows(IllegalArgumentException.class,
            () -> CategoryMapper.fromHref().map("unknown_href"));
  }

  private static Stream<String> hrefsProvider() {
    return MAPPING.values().stream();
  }

  private static Category getCategoryBy(final String href) {
    return MAPPING.inverse().get(href);
  }
}
