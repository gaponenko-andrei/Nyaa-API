package agp.nyaa.api.mapper;

import agp.nyaa.api.util.SupportedValuesAware;

public interface SupportedValuesAwareMapper<T, U> extends Mapper<T, U>, SupportedValuesAware<T> {
}
