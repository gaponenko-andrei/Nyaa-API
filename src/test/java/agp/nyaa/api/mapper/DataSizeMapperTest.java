package agp.nyaa.api.mapper;

import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.model.DataSize.Unit;
import agp.nyaa.api.test.TestDataSizeUnitMapper;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.testng.annotations.Test;

import static agp.nyaa.api.model.DataSize.Unit.BYTE;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

public class DataSizeMapperTest {

  private final DataSizeUnitMapper testUnitMapper =
    new TestDataSizeUnitMapper().from("TestUnit").to(Unit.BYTE);

  @Test
  public void mappingCallsDataSizeUnitMapper() {

    /* Arrange */
    val mapper = DataSizeMapper.using(spy(testUnitMapper));

    /* Act */
    mapper.map("100 TestUnit");

    /* Assert */
    verify(mapper.getUnitMapper()).map("TestUnit");
  }

  @Test
  public void mappingOfIntegerDataSizeString() {
    testUnitStringMappingExpectedResult("1 TestUnit", DataSize.of(1, BYTE));
  }

  @Test
  public void mappingOfFloatDataSizeStringWithZeroFraction() {
    testUnitStringMappingExpectedResult("1.0 TestUnit", DataSize.of(1.0f, BYTE));
  }

  @Test
  public void mappingOfFloatDataSizeStringWithNonZeroFraction() {
    testUnitStringMappingExpectedResult("20.20 TestUnit", DataSize.of(20.20f, BYTE));
  }

  @Test
  public void mappingOfIntegerDataSizeStringWithAdditionalSpaces() {
    testUnitStringMappingExpectedResult("  300   TestUnit  ", DataSize.of(300, BYTE));
  }

  @Test
  public void mappingOfFloatDataSizeStringWithAdditionalSpaces() {
    testUnitStringMappingExpectedResult("  300.300   TestUnit  ", DataSize.of(300.300f, BYTE));
  }

  private void testUnitStringMappingExpectedResult(final String inputString,
                                                   final DataSize expectedResult) {

    /* Arrange */
    val mapper = DataSizeMapper.using(testUnitMapper);

    /* Act */
    final DataSize actualMappingResult = mapper.map(inputString);

    /* Assert */
    assertEquals(actualMappingResult, expectedResult);
  }

  /* Negative scenarios */

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNullUnitMapper() {
    DataSizeMapper.using((DataSizeUnitMapper) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnUnitMapperWithoutSupportedUnits() {
    DataSizeMapper.using(DataSizeUnitMapper.from(ImmutableMap.of(/* no supported units */)));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnDataSizeStringWithUnsupportedUnit() {
    DataSizeMapper.using(testUnitMapper).map("100 UnsupportedUnits");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnIntegerDataSizeStringWithZeroUnits() {
    DataSizeMapper.using(testUnitMapper).map("0 TestUnit");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnFloatDataSizeStringWithZeroUnits() {
    DataSizeMapper.using(testUnitMapper).map("0.0 TestUnit");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNegativeIntegerDataSize() {
    DataSizeMapper.using(testUnitMapper).map("-10 TestUnit");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNonDecimalDataSizeString() {
    DataSizeMapper.using(testUnitMapper).map("0x1 TestUnit");
  }
}
