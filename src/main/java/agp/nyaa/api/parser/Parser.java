package agp.nyaa.api.parser;

import org.jsoup.nodes.Element;

@FunctionalInterface
public interface Parser<T> {
    T parse(Element element);
}
