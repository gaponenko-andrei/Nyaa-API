package agp.nyaa.api.mapper;

import agp.nyaa.api.TestCases;
import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class DataSizeUnitMapperTest {

  private static final ImmutableMap<String, DataSize.Unit> MAPPING =
          ImmutableMap.<String, DataSize.Unit>builder()
                  .put("Bytes", DataSize.Unit.BYTE)
                  .put("KiB", DataSize.Unit.KILOBYTE)
                  .put("MiB", DataSize.Unit.MEGABYTE)
                  .put("GiB", DataSize.Unit.GIGABYTE)
                  .put("TiB", DataSize.Unit.TERABYTE)
                  .build();

  private final DataSizeUnitMapper<String, DataSize.Unit> mapper = DataSizeUnitMapper.fromSiteValue();


  @Test(dataProvider = "siteValuesProvider")
  public void fromSiteValueMapping(final String siteValue) {

    /* Act */
    val unit = mapper.map(siteValue);

    /* Assert */
    assertEquals(unit, getUnitBy(siteValue));
  }

  @Test
  public void fromSiteValueMapperThrowsOnNullArgument() {
    assertThrows(NullPointerException.class, () -> mapper.map(null));
  }

  @Test
  public void fromSiteValueMapperThrowsOnUnknownArgument() {
    assertThrows(IllegalArgumentException.class, () -> mapper.map("unknown_arg"));
  }

  @DataProvider(name = "siteValuesProvider")
  private static Iterator<Object[]> getTestSiteValues() {
    return TestCases.from(MAPPING.keySet());
  }

  private static DataSize.Unit getUnitBy(final String siteValue) {
    return MAPPING.get(siteValue);
  }
}
