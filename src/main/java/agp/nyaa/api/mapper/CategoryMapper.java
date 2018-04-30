package agp.nyaa.api.mapper;

import agp.nyaa.api.model.Category;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class CategoryMapper<T, R> implements Mapper<T, R> {

  public static CategoryMapper<Category, String> toHref() {
    return new ToHrefMapper();
  }

  public static CategoryMapper<String, Category> fromHref() {
    return new FromHrefMapper();
  }

  private static final class ToHrefMapper extends CategoryMapper<Category, String> {

    private static final ImmutableMap<Category, String> MAPPING =
            ImmutableMap.<Category, String>
                    builder()
                    .put(Category.Anime.MusicVideo.getInstance(), "/?c=1_1")
                    .put(Category.Anime.EnglishTranslated.getInstance(), "/?c=1_2")
                    .put(Category.Anime.NonEnglishTranslated.getInstance(), "/?c=1_3")
                    .put(Category.Anime.NonTranslated.getInstance(), "/?c=1_4")
                    .build();

    @Override
    public String apply(@NonNull final Category category) {
      checkArgument(isKnown(category), "Unknown 'category': %s.");
      return MAPPING.get(category);
    }

    private static boolean isKnown(final Category category) {
      return MAPPING.containsKey(category);
    }
  }

  private static final class FromHrefMapper extends CategoryMapper<String, Category> {

    private static final ImmutableMap<String, Category> MAPPING =
            ImmutableMap.<String, Category>builder()
                    .put("/?c=1_1", Category.Anime.MusicVideo.getInstance())
                    .put("/?c=1_2", Category.Anime.EnglishTranslated.getInstance())
                    .put("/?c=1_3", Category.Anime.NonEnglishTranslated.getInstance())
                    .put("/?c=1_4", Category.Anime.NonTranslated.getInstance())
                    .build();

    @Override
    public Category apply(@NonNull final String href) {
      checkArgument(isKnown(href), "Unknown 'href': %s.");
      return MAPPING.get(href);
    }

    private static boolean isKnown(final String href) {
      return MAPPING.containsKey(href);
    }
  }
}
