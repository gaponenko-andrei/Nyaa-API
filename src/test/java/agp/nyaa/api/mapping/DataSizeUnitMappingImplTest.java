package agp.nyaa.api.mapping;

import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.test.TestCases;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;

public class DataSizeUnitMappingImplTest {

  private static final ImmutableMap<String, DataSize.Unit> TESTED_MAPPINGS =
    ImmutableMap.<String, DataSize.Unit>builder()
      .put("Bytes", DataSize.Unit.BYTE)
      .put("KiB", DataSize.Unit.KILOBYTE)
      .put("MiB", DataSize.Unit.MEGABYTE)
      .put("GiB", DataSize.Unit.GIGABYTE)
      .put("TiB", DataSize.Unit.TERABYTE)
      .build();

  private DataSizeUnitMapping mapping = DataSizeUnitMapping.impl();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnNull() {
    mapping.apply(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnUnsupportedUnitStrings() {
    mapping.apply("unsupported");
  }

  @Test
  public void supportedValuesShouldMatchTestedValues() {
    assertEquals(mapping.supportedValues(), TESTED_MAPPINGS.keySet());
  }

  @Test(dataProvider = "unitTestCasesProvider")
  public void mappingSupportedUnitStringShouldProduceExpectedResult(final String unitString) {

    // given
    final DataSize.Unit actualMappingResult = mapping.apply(unitString);

    // expect
    assertEquals(actualMappingResult, getExpectedResultBy(unitString));
  }

  @DataProvider(name = "unitTestCasesProvider")
  private static Iterator<Object[]> getTestSiteValues() {
    return TestCases.from(TESTED_MAPPINGS.keySet());
  }

  private static DataSize.Unit getExpectedResultBy(final String siteValue) {
    return TESTED_MAPPINGS.get(siteValue);
  }
}
