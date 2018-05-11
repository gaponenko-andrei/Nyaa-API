package agp.nyaa.api.mapper;

import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.model.DataSize.Unit;
import agp.nyaa.api.test.TestCases;
import agp.nyaa.api.test.TestDataSize;
import agp.nyaa.api.test.TestDataSizeUnitMapper;
import lombok.val;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static agp.nyaa.api.model.DataSize.Unit.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

public class DataSizeMapperTest {

  private DataSizeMapper mapper;
  private String dataSize;
  private DataSize mappingResult;

  @AfterMethod
  public void tearDown() {
    mapper = null;
    dataSize = null;
    mappingResult = null;
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void constructorThrowsOnNullUnitMapper() {
    new DataSizeMapper(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorThrowsOnUnitMapperWithoutSupportedUnits() {
    new DataSizeMapper(new TestDataSizeUnitMapper.WithoutSupportedUnits());
  }

  @Test
  public void mappingCallsDataSizeUnitMapper() {

    /* Arrange */
    givenDataSizeMapperWith(spy(new TestDataSizeUnitMapper.ToRandomUnit()));
    givenDataSizeIs(new TestDataSize().withRandomValue().withUnitSupportedBy(mapper));

    /* Act */
    mapDataSize();

    /* Assert */
    assertUnitMapperWasCalled();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnDataSizeWithUnsupportedUnit() {

    /* Arrange */
    givenDataSizeMapperWith(TestDataSizeUnitMapper.to(Unit.BYTE));
    givenDataSizeIs(new TestDataSize().withRandomValue().withUnit("unsupported"));

    /* Act */
    mapDataSize();
  }

  @Test(
    dataProvider = "invalidDataSizeTestCasesProvider",
    expectedExceptions = IllegalArgumentException.class)
  public void throwsOnInvalidDataSize(final String dataSize) {

    /* Arrange */
    givenDataSizeMapperWith(new TestDataSizeUnitMapper.ToRandomUnit());
    givenDataSizeIs(dataSize);

    /* Act */
    mapDataSize();
  }

  @Test(dataProvider = "validDataSizeTestCasesProvider")
  public void mapping(final String dataSize, final DataSize expected) {

    /* Arrange */
    givenDataSizeMapperWith(TestDataSizeUnitMapper.to(expected.unit()));
    givenDataSizeIs(dataSize);

    /* Act */
    mapDataSize();

    /* Assert */
    assertMappingResultIs(expected);
  }

  private void givenDataSizeIs(final String dataSize) {
    this.dataSize = dataSize;
  }

  private void givenDataSizeIs(final TestDataSize dataSize) {
    this.dataSize = dataSize.toString();
  }

  private void givenDataSizeMapperWith(final DataSizeUnitMapper dataSizeUnitMapper) {
    this.mapper = new DataSizeMapper(dataSizeUnitMapper);
  }

  private void mapDataSize() {
    this.mappingResult = mapper.map(dataSize);
  }

  private void assertUnitMapperWasCalled() {
    verify(mapper.getUnitMapper()).map(anyString());
  }

  private void assertMappingResultIs(final DataSize dataSize) {
    assertEquals(mappingResult, dataSize);
  }

  @DataProvider(name = "invalidDataSizeTestCasesProvider")
  private static Iterator<Object[]> getIllegalArgumentTestCases() {
    return TestCases.forEach(
      new TestDataSize().withoutUnit().withoutValue().toString(),
      new TestDataSize().withValue(100).withoutUnit().toString(),
      new TestDataSize().withUnit(BYTE).withoutValue().toString()
    );
  }

  @DataProvider(name = "validDataSizeTestCasesProvider")
  private static Iterator<Object[]> getValidDataSizeTestCases() {
    final List<Object[]> testCases = new ArrayList<>();

    testCases.add(newTestCaseFor(100, BYTE));
    testCases.add(newTestCaseFor(200, KILOBYTE));
    testCases.add(newTestCaseFor(300, MEGABYTE));
    testCases.add(newTestCaseFor(400, GIGABYTE));
    testCases.add(newTestCaseFor(500, TERABYTE));
    testCases.add(newTestCaseFor(100, BYTE, "  %s  %s  "));

    return testCases.iterator();
  }

  private static Object[] newTestCaseFor(final Integer value,
                                         final Unit unit) {

    val defaultDataSizeTemplate = "%s %s";
    return newTestCaseFor(value, unit, defaultDataSizeTemplate);
  }

  private static Object[] newTestCaseFor(final Integer value,
                                         final Unit unit,
                                         final String template) {

    val dataSize = TestDataSize.from(value, unit).toFormattedStringBy(template);
    val expected = new DataSize(value, unit);
    return new Object[]{dataSize, expected};
  }
}
