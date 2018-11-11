package agp.nyaa.api.model;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DataSizeTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorShouldThrowOnNullIntegerValues() {
    DataSize.of((Integer) null, DataSize.Unit.BYTE);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorShouldThrowOnNullFloatValues() {
    DataSize.of((Float) null, DataSize.Unit.BYTE);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructorShouldThrowOnNullUnits() {
    DataSize.of(1, null);
  }

  @Test
  public void integerDataSizeShouldEqualFloatDataSizeWithZeroFractionValue() {
    for (DataSize.Unit unit : DataSize.Unit.values()) {
      assertEquals(DataSize.of(1, unit), DataSize.of(1f, unit));
      assertEquals(DataSize.of(1, unit), DataSize.of(1.0f, unit));
    }
  }
}
