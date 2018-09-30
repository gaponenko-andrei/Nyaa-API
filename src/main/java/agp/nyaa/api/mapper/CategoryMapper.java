package agp.nyaa.api.mapper;

import agp.nyaa.api.model.Category;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;

public final class CategoryMapper implements SupportedValuesAwareMapper<String, Category> {

  private static final ImmutableMap<String, Category> MAPPING =
    ImmutableMap.<String, Category>builder()
      .put("/?c=1_1", Category.Anime.MusicVideo.instance())
      .put("/?c=1_2", Category.Anime.EnglishTranslated.instance())
      .put("/?c=1_3", Category.Anime.NonEnglishTranslated.instance())
      .put("/?c=1_4", Category.Anime.NonTranslated.instance())
      .build();


  public static Category applicationTo(@NonNull final String href) {
    return new CategoryMapper().apply(href);
  }

  @Override
  public Category apply(@NonNull final String href) {
    checkArgument(isSupported(href), "Unsupported 'href': %s.", href);
    return MAPPING.get(href);
  }

  @Override
  public ImmutableSet<String> supportedValues() {
    return MAPPING.keySet();
  }

  private boolean isSupported(final String href) {
    return supportedValues().contains(href);
  }
}
