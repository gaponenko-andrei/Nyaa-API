package agp.nyaa.api.mapper;

import java.util.function.Function;

@FunctionalInterface
public interface Mapper<T, U> extends Function<T, U> {

  default U map(T value) {
    return this.apply(value);
  }
}
