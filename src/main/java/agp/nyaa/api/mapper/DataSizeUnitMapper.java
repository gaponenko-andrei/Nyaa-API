package agp.nyaa.api.mapper;

import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class DataSizeUnitMapper<T, R> implements Mapper<T, R> {

  public static DataSizeUnitMapper<String, DataSize.Unit> fromSiteValue() {
    return new FromSiteValueMapper();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  private static final class FromSiteValueMapper extends DataSizeUnitMapper<String, DataSize.Unit> {

    private static final ImmutableMap<String, DataSize.Unit> MAPPING =
            ImmutableMap.<String, DataSize.Unit>builder()
                    .put("Bytes", DataSize.Unit.BYTE)
                    .put("KiB", DataSize.Unit.KILOBYTE)
                    .put("MiB", DataSize.Unit.MEGABYTE)
                    .put("GiB", DataSize.Unit.GIGABYTE)
                    .put("TiB", DataSize.Unit.TERABYTE)
                    .build();

    @Override
    public DataSize.Unit apply(@NonNull final String siteValue) {
      checkArgument(isKnown(siteValue), "Unknown 'siteValue': %s.");
      return MAPPING.get(siteValue);
    }

    private static boolean isKnown(final String siteValue) {
      return MAPPING.containsKey(siteValue);
    }
  }

}
