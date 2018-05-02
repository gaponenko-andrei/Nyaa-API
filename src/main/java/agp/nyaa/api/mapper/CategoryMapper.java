package agp.nyaa.api.mapper;

import agp.nyaa.api.model.Category;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class CategoryMapper<T, R> implements Mapper<T, R> {

  public static CategoryMapper<String, Category> fromHref() {
    return new FromHrefMapper();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
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
