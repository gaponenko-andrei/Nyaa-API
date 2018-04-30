package agp.nyaa.api.model;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class Category {

  @Getter
  @NonNull
  private final String name;

  private Category(@NonNull final String name) {
    this.name = name;
  }

  public static class Anime extends Category {

    private static final Anime INSTANCE = new Anime();

    public static Anime getInstance() {
      return INSTANCE;
    }

    private Anime() {
      this("Anime");
    }

    private Anime(@NonNull final String name) {
      super(name);
    }

    public static final class MusicVideo extends Anime {

      private static final MusicVideo INSTANCE = new MusicVideo();

      public static MusicVideo getInstance() {
        return INSTANCE;
      }

      private MusicVideo() {
        super("Anime Music Video");
      }
    }

    public static final class EnglishTranslated extends Anime {

      private static final EnglishTranslated INSTANCE = new EnglishTranslated();

      public static EnglishTranslated getInstance() {
        return INSTANCE;
      }

      private EnglishTranslated() {
        super("English Translated Anime");
      }
    }

    public static final class NonEnglishTranslated extends Anime {

      private static final NonEnglishTranslated INSTANCE = new NonEnglishTranslated();

      public static NonEnglishTranslated getInstance() {
        return INSTANCE;
      }

      private NonEnglishTranslated() {
        super("Non English Translated Anime");
      }
    }

    public static final class NonTranslated extends Anime {

      private static final NonTranslated INSTANCE = new NonTranslated();

      public static NonTranslated getInstance() {
        return INSTANCE;
      }

      private NonTranslated() {
        super("Non Translated Anime");
      }
    }
  }
}
