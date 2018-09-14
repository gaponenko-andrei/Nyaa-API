package agp.nyaa.api.mapper;

import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;

public interface DataSizeUnitMapper extends SupportedValuesAwareMapper<String, DataSize.Unit> {

  static DataSizeUnitMapper impl() {
    return DataSizeUnitMapper.from(
      ImmutableMap.<String, DataSize.Unit>builder()
        .put("Bytes", DataSize.Unit.BYTE)
        .put("KiB", DataSize.Unit.KILOBYTE)
        .put("MiB", DataSize.Unit.MEGABYTE)
        .put("GiB", DataSize.Unit.GIGABYTE)
        .put("TiB", DataSize.Unit.TERABYTE)
        .build());
  }

  static DataSizeUnitMapper from(@NonNull final ImmutableMap<String, DataSize.Unit> mapping) {
    return new DataSizeUnitMapper() {

      @Override
      public DataSize.Unit apply(@NonNull final String siteValue) {
        checkArgument(isSupported(siteValue), "Unsupported 'siteValue': %s.", siteValue);
        return mapping.get(siteValue);
      }

      @Override
      public ImmutableSet<String> supportedValues() {
        return mapping.keySet();
      }

      private boolean isSupported(final String siteValue) {
        return supportedValues().contains(siteValue);
      }
    };
  }
}
