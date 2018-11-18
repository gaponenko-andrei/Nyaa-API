package agp.nyaa.api.parsing;

import java.util.function.Function;

import agp.nyaa.api.element.ImmutableElement;

// todo Parser -> Parsing
@FunctionalInterface
public interface Parser<T extends ImmutableElement, U> extends Function<T, U> {
  default U parse(T element) {
    return this.apply(element);
  }
}
