package agp.nyaa.api.mapper;

import agp.nyaa.api.TestCases;
import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;

public class DataSizeUnitMapperTest {

  private static final ImmutableMap<String, DataSize.Unit> MAPPING =
    ImmutableMap.<String, DataSize.Unit>builder()
      .put("Bytes", DataSize.Unit.BYTE)
      .put("KiB", DataSize.Unit.KILOBYTE)
      .put("MiB", DataSize.Unit.MEGABYTE)
      .put("GiB", DataSize.Unit.GIGABYTE)
      .put("TiB", DataSize.Unit.TERABYTE)
      .build();

  private DataSizeUnitMapper mapper;
  private String unit;
  private DataSize.Unit mappingResult;

  @BeforeMethod
  public void setUp() {
    mapper = new DataSizeUnitMapper();
  }

  @Test(dataProvider = "unitTestCasesProvider")
  public void mapping(final String unit) {

    /* Arrange */
    givenUnitIs(unit);

    /* Act */
    mapUnit();

    /* Assert */
    assertExpectedResult();
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void throwsOnNullArgument() {

    /* Arrange */
    givenUnitIs(null);

    /* Act */
    mapUnit();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnUnknownArgument() {

    /* Arrange */
    givenUnitIs("unknown");

    /* Act */
    mapUnit();
  }

  private void givenUnitIs(final String unit) {
    this.unit = unit;
  }

  private void mapUnit() {
    mappingResult = mapper.map(unit);
  }

  private void assertExpectedResult() {
    assertEquals(mappingResult, getExpectedResultBy(unit));
  }

  @DataProvider(name = "unitTestCasesProvider")
  private static Iterator<Object[]> getTestSiteValues() {
    return TestCases.from(MAPPING.keySet());
  }

  private static DataSize.Unit getExpectedResultBy(final String siteValue) {
    return MAPPING.get(siteValue);
  }
}
