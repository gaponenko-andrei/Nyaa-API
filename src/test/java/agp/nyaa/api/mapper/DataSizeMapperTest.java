package agp.nyaa.api.mapper;

import static agp.nyaa.api.model.DataSize.Unit.BYTE;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.model.DataSize.Unit;
import agp.nyaa.api.test.TestCases;
import agp.nyaa.api.test.TestDataSizeUnitMapper;
import lombok.val;

public class DataSizeMapperTest {

  private final DataSizeUnitMapper testUnitMapper =
    new TestDataSizeUnitMapper().from("TestUnit").to(Unit.BYTE);


  @Test
  public void mappingDataSizeStringWithIntegerValueShouldProduceExpectedResult() {
    testMappingProducesExpectedResult("1 TestUnit", DataSize.of(1, BYTE));
  }

  @Test
  public void mappingDataSizeStringWithFloatValueShouldProduceExpectedResult() {
    testMappingProducesExpectedResult("1.0 TestUnit", DataSize.of(1.0f, BYTE));
    testMappingProducesExpectedResult("20.20 TestUnit", DataSize.of(20.20f, BYTE));
  }

  @Test
  public void mappingDataSizeStringWithAdditionalSpacesShouldProduceExpectedResult() {
    testMappingProducesExpectedResult("  300   TestUnit  ", DataSize.of(300, BYTE));
    testMappingProducesExpectedResult("  300.300   TestUnit  ", DataSize.of(300.300f, BYTE));
  }

  private void testMappingProducesExpectedResult(final String inputString,
                                                 final DataSize expectedResult) {

    // given
    val mapper = DataSizeMapper.using(testUnitMapper);

    // when
    final DataSize actualMappingResult = mapper.map(inputString);

    // then
    assertEquals(actualMappingResult, expectedResult);
  }

  @Test
  public void dataSizeMapperShouldUseProvidedUnitMapper() {

    // given
    val mapper = DataSizeMapper.using(spy(testUnitMapper));

    // when
    mapper.map("100 TestUnit");

    // then
    verify(mapper.getUnitMapper()).map("TestUnit");
  }

  /* Negative scenarios */

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void usingNullUnitMapperShouldThrow() {
    DataSizeMapper.using((DataSizeUnitMapper) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void usingUnitMapperWithoutSupportedUnitsShouldThrow() {

    // given
    val unitMapper = DataSizeUnitMapper.from(emptyMap());
    assertTrue(unitMapper.supportedValues().isEmpty());

    // when
    DataSizeMapper.using(unitMapper);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingDataSizeStringWithUnsupportedUnitShouldThrow() {
    DataSizeMapper.using(testUnitMapper).map("100 UnsupportedUnits");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingDataSizeStringWithNonDecimalUnitsShouldThrow() {
    DataSizeMapper.using(testUnitMapper).map("0x1 TestUnit");
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
    dataProvider = "providerOfDataSizeStringsWithZeroValues")
  public void mappingDataSizeStringWithZeroValueShouldThrow(final String value) {
    DataSizeMapper.using(testUnitMapper).map(value);
  }

  @DataProvider(name = "providerOfDataSizeStringsWithZeroValues")
  private Iterator<Object[]> getDataSizeStringsWithZeroValues() {
    return TestCases.forEach("0 TestUnit", "0.0 TestUnit");
  }

  @Test(expectedExceptions = IllegalArgumentException.class,
    dataProvider = "providerOfDataSizeStringsWithNegativeValues")
  public void mappingDataSizeStringWithNegativeValueShouldThrow(final String value) {
    DataSizeMapper.using(testUnitMapper).map(value);
  }

  @DataProvider(name = "providerOfDataSizeStringsWithNegativeValues")
  private Iterator<Object[]> getDataSizeStringsWithNegativeValues() {
    return TestCases.forEach("-10 TestUnit", "-10.0 TestUnit");
  }
}
