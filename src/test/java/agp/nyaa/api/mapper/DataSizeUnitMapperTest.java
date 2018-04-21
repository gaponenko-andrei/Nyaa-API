package agp.nyaa.api.mapper;

import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableBiMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import properties.Constants;

import static org.junit.jupiter.api.Assertions.*;

class DataSizeUnitMapperTest {

  private static final ImmutableBiMap<DataSize.Unit, String> MAPPING =
          ImmutableBiMap.<DataSize.Unit, String>
                  builder()
                  .put(DataSize.Unit.BYTE, Constants.data.size.bytes.siteValue)
                  .put(DataSize.Unit.KILOBYTE, Constants.data.size.kilobytes.siteValue)
                  .put(DataSize.Unit.MEGABYTE, Constants.data.size.megabytes.siteValue)
                  .put(DataSize.Unit.GIGABYTE, Constants.data.size.gigabytes.siteValue)
                  .put(DataSize.Unit.TERABYTE, Constants.data.size.terabytes.siteValue)
                  .build();

  //
  // Data.Size.Unit -> Site Value
  //

  @ParameterizedTest
  @EnumSource(DataSize.Unit.class)
  void toSiteValueMapping(final DataSize.Unit dataSizeUnit) {

    /* Arrange */
    final Mapper<DataSize.Unit, String> mapper = DataSizeUnitMapper.toSiteValue();

    /* Act */
    final String mappingResult = mapper.map(dataSizeUnit);

    /* Assert */
    assertEquals(getSiteValueBy(dataSizeUnit), mappingResult);
  }

  @Test
  void toSizeValueMapperThrowsOnNullArgument() {
    assertThrows(NullPointerException.class,
            () -> DataSizeUnitMapper.toSiteValue().map(null));
  }

  private static String getSiteValueBy(final DataSize.Unit dataSizeUnit) {
    return MAPPING.get(dataSizeUnit);
  }
}
