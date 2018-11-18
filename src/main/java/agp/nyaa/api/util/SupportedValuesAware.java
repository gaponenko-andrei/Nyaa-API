package agp.nyaa.api.util;

import com.google.common.collect.ImmutableSet;

@FunctionalInterface
public interface SupportedValuesAware<T> {
  ImmutableSet<T> supportedValues();

  default boolean isSupported(T value) {
    return supportedValues().contains(value);
  }
}