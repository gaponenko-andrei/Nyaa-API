package agp.nyaa.api.mapping;

import java.util.function.Function;

import agp.nyaa.api.util.SupportedValuesAware;

public interface SupportedValuesAwareMapping<T, U> extends Function<T, U>, SupportedValuesAware<T> {
}
