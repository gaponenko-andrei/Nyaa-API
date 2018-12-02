package agp.nyaa.api.mapping;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import agp.nyaa.api.model.DataSize;
import lombok.NonNull;

public interface DataSizeUnitMapping extends SupportedValuesAwareMapping<String, DataSize.Unit> {

  static DataSizeUnitMapping impl() {
    return DataSizeUnitMapping.from(
      ImmutableMap.<String, DataSize.Unit>builder()
        .put("Bytes", DataSize.Unit.BYTE)
        .put("KiB", DataSize.Unit.KILOBYTE)
        .put("MiB", DataSize.Unit.MEGABYTE)
        .put("GiB", DataSize.Unit.GIGABYTE)
        .put("TiB", DataSize.Unit.TERABYTE)
        .build());
  }

  static DataSizeUnitMapping from(@NonNull final Map<String, DataSize.Unit> mappingSource) {
    return new DataSizeUnitMapping() {

      private ImmutableMap<String, DataSize.Unit> mapping = ImmutableMap.copyOf(mappingSource);

      @Override
      public DataSize.Unit apply(@NonNull final String siteValue) {
        checkArgument(isSupported(siteValue), "Unsupported 'siteValue': %s.", siteValue);
        return mapping.get(siteValue);
      }

      @Override
      public ImmutableSet<String> supportedValues() {
        return mapping.keySet();
      }
    };
  }
}
