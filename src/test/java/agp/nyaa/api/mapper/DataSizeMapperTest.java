package agp.nyaa.api.mapper;

import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.model.DataSize.Unit;
import agp.nyaa.api.test.TestDataSizeUnitMapper;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static agp.nyaa.api.model.DataSize.Unit.BYTE;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

public class DataSizeMapperTest {

  private DataSizeMapper mapper;
  private String dataSizeString;
  private DataSize mappingResult;

  @AfterMethod
  public void tearDown() {
    mapper = null;
    dataSizeString = null;
    mappingResult = null;
  }

  @Test(dataProvider = "validDataSizeTestCasesProvider")
  public void mapping(final String dataSizeString,
                      final DataSize expectedDataSize,
                      final DataSizeUnitMapper unitMapper) {
    /* Arrange */
    givenDataSizeStringIs(dataSizeString);
    givenDataSizeMapperWith(unitMapper);

    /* Act */
    mapDataSize();

    /* Assert */
    assertMappingResultIs(expectedDataSize);
  }

  @Test
  public void mappingCallsDataSizeUnitMapper() {

    /* Arrange */
    val unitMapper = TestDataSizeUnitMapper.from("Random Units").to(BYTE);
    givenDataSizeMapperWith(spy(unitMapper));
    givenDataSizeStringIs("100 Random Units");

    /* Act */
    mapDataSize();

    /* Assert */
    assertUnitMapperWasCalled();
  }

  //
  // Negative scenarios
  //

  @Test(expectedExceptions = NullPointerException.class)
  public void constructorThrowsOnNullUnitMapper() {
    new DataSizeMapper(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorThrowsOnUnitMapperWithoutSupportedUnits() {
    new DataSizeMapper(DataSizeUnitMapper.from(ImmutableMap.of()));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnDataSizeWithUnsupportedUnit() {

    /* Arrange */
    val unitMapper = TestDataSizeUnitMapper.from("Byte").to(BYTE);
    givenDataSizeMapperWith(unitMapper);
    givenDataSizeStringIs("100 Kilobyte");

    /* Act */
    mapDataSize();
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
        dataProvider = "invalidDataSizeTestCasesProvider")
  public void throwsOnInvalidDataSize(final String dataSizeString,
                                      final DataSizeUnitMapper unitMapper) {
    /* Arrange */
    givenDataSizeMapperWith(unitMapper);
    givenDataSizeStringIs(dataSizeString);

    /* Act */
    mapDataSize();
  }

  //
  // Additional methods
  //

  private void givenDataSizeStringIs(final String dataSizeString) {
    this.dataSizeString = dataSizeString;
  }

  private void givenDataSizeMapperWith(final DataSizeUnitMapper dataSizeUnitMapper) {
    this.mapper = new DataSizeMapper(dataSizeUnitMapper);
  }

  private void mapDataSize() {
    this.mappingResult = mapper.map(dataSizeString);
  }

  private void assertUnitMapperWasCalled() {
    verify(mapper.getUnitMapper()).map(anyString());
  }

  private void assertMappingResultIs(final DataSize dataSize) {
    assertEquals(mappingResult, dataSize);
  }

  //
  // Data Providers
  //

  @DataProvider(name = "invalidDataSizeTestCasesProvider")
  private static Iterator<Object[]> getIllegalArgumentTestCases() {
    final List<Object[]> testCases = new ArrayList<>();

    val unitMapper = DataSizeUnitMapper.from(
      ImmutableMap.<String, Unit>builder()
        .put("Unit1", Unit.BYTE)
        .put("Unit2", Unit.KILOBYTE)
        .put("Unit3", Unit.MEGABYTE)
        .put("Unit4", Unit.GIGABYTE)
        .build());

    String dataSizeString;

    // Test case for mapping integer zero size value
    dataSizeString = "0 Unit1";
    testCases.add(new Object[] {dataSizeString, unitMapper});

    // Test case for mapping real number zero size value
    dataSizeString = "0.0 Unit2";
    testCases.add(new Object[] {dataSizeString, unitMapper});

    // Test case for mapping negative size value
    dataSizeString = "-300 Unit3";
    testCases.add(new Object[] {dataSizeString, unitMapper});

    // Test case for mapping size value with illegal chars
    dataSizeString = Float.MAX_VALUE + " Unit4";
    testCases.add(new Object[] {dataSizeString, unitMapper});

    // Test case for mapping data size string without value
    dataSizeString = "Unit4";
    testCases.add(new Object[] {dataSizeString, unitMapper});

    return testCases.iterator();
  }

  @DataProvider(name = "validDataSizeTestCasesProvider")
  private static Iterator<Object[]> getValidDataSizeTestCases() {
    final List<Object[]> testCases = new ArrayList<>();

    val unitMapper = DataSizeUnitMapper.from(
      ImmutableMap.<String, Unit>builder()
        .put("Unit1", Unit.BYTE)
        .put("Unit2", Unit.KILOBYTE)
        .put("Unit3", Unit.MEGABYTE)
        .put("Unit4", Unit.GIGABYTE)
        .build());

    String dataSizeString;
    DataSize expectedDataSize;

    // Test case for mapping integer size value of Unit1 → BYTE
    dataSizeString = "1 Unit1";
    expectedDataSize = DataSize.of(1f, Unit.BYTE);
    testCases.add(new Object[] {dataSizeString, expectedDataSize, unitMapper});

    // Test case for mapping real number size value of Unit2 → KILOBYTE
    dataSizeString = "20.020 Unit2";
    expectedDataSize = DataSize.of(20.020f, Unit.KILOBYTE);
    testCases.add(new Object[] {dataSizeString, expectedDataSize, unitMapper});

    // Test case for mapping integer size value of Unit3 → MEGABYTE
    // with some additional spaces present in the string
    dataSizeString = "  300   Unit3  ";
    expectedDataSize = DataSize.of(300f, Unit.MEGABYTE);
    testCases.add(new Object[] {dataSizeString, expectedDataSize, unitMapper});

    // Test case for mapping real number size value of Unit4 → GIGABYTE
    // with some additional spaces present in the string
    dataSizeString = "  300.300   Unit4  ";
    expectedDataSize = DataSize.of(300.300f, Unit.GIGABYTE);
    testCases.add(new Object[] {dataSizeString, expectedDataSize, unitMapper});

    return testCases.iterator();
  }
}
