package agp.nyaa.api.mapper;

import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.test.TestCases;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;

public class DataSizeUnitMapperImplTest {

  private static final ImmutableMap<String, DataSize.Unit> MAPPING =
    ImmutableMap.<String, DataSize.Unit>builder()
      .put("Bytes", DataSize.Unit.BYTE)
      .put("KiB", DataSize.Unit.KILOBYTE)
      .put("MiB", DataSize.Unit.MEGABYTE)
      .put("GiB", DataSize.Unit.GIGABYTE)
      .put("TiB", DataSize.Unit.TERABYTE)
      .build();

  private DataSizeUnitMapper mapper = DataSizeUnitMapper.impl();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnNulls() {
    mapper.map(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnUnsupportedUnitStrings() {
    mapper.map("unsupported");
  }

  @Test
  public void supportedValuesShouldMatchTestedValues() {
    assertEquals(mapper.supportedValues(), MAPPING.keySet());
  }

  @Test(dataProvider = "unitTestCasesProvider")
  public void mappingSupportedUnitStringShouldProduceExpectedResult(final String unitString) {

    // Given
    final DataSize.Unit actualMappingResult = mapper.map(unitString);

    // Expect
    assertEquals(actualMappingResult, getExpectedResultBy(unitString));
  }

  @DataProvider(name = "unitTestCasesProvider")
  private static Iterator<Object[]> getTestSiteValues() {
    return TestCases.from(MAPPING.keySet());
  }

  private static DataSize.Unit getExpectedResultBy(final String siteValue) {
    return MAPPING.get(siteValue);
  }
}
