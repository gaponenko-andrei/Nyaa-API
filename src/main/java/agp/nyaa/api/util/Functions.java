package agp.nyaa.api.util;

import java.util.function.Function;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Functions {

  public static <T, R> Function<T, R> to(@NonNull final Function<T, R> f) {
    return f;
  }

  public static <T, R> Function<T, R> as(@NonNull final Function<T, R> f) {
    return f;
  }
}
