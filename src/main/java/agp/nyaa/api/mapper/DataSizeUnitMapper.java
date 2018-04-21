package agp.nyaa.api.mapper;

import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import properties.Constants;

public abstract class DataSizeUnitMapper<T, R> implements Mapper<T, R> {

  public static Mapper<DataSize.Unit, String> toSiteValue() {
    return new ToSiteValueMapper();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  private static final class ToSiteValueMapper extends DataSizeUnitMapper<DataSize.Unit, String> {

    private static final ImmutableMap<DataSize.Unit, String> MAPPING =
            ImmutableMap.<DataSize.Unit, String>
                    builder()
                    .put(DataSize.Unit.BYTE, Constants.data.size.bytes.siteValue)
                    .put(DataSize.Unit.KILOBYTE, Constants.data.size.kilobytes.siteValue)
                    .put(DataSize.Unit.MEGABYTE, Constants.data.size.megabytes.siteValue)
                    .put(DataSize.Unit.GIGABYTE, Constants.data.size.gigabytes.siteValue)
                    .put(DataSize.Unit.TERABYTE, Constants.data.size.terabytes.siteValue)
                    .build();

    @Override
    public String apply(@NonNull DataSize.Unit unit) {
      return MAPPING.get(unit);
    }
  }



}
