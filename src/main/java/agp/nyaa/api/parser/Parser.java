package agp.nyaa.api.parser;

import org.jsoup.nodes.Element;

import java.util.function.Function;

@FunctionalInterface
public interface Parser<T extends Element, U> extends Function<T, U> {
  default U parse(T element) {
    return this.apply(element);
  }
}
