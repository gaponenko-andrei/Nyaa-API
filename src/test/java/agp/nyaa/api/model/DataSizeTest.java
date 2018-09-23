package agp.nyaa.api.model;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DataSizeTest {

  @Test(expectedExceptions = NullPointerException.class)
  public void constructionThrowsOnNullIntegerValue() {
    DataSize.of((Integer) null, DataSize.Unit.BYTE);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void constructionThrowsOnNullFloatValue() {
    DataSize.of((Float) null, DataSize.Unit.BYTE);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void constructionThrowsOnNullUnitValue() {
    DataSize.of(1, null);
  }

  @Test
  public void integerDataSizeEqualsFloatDataSizeWithZeroFractionValue() {
    for (DataSize.Unit unit : DataSize.Unit.values()) {
      assertEquals(DataSize.of(1, unit), DataSize.of(1f, unit));
      assertEquals(DataSize.of(1, unit), DataSize.of(1.0f, unit));
    }
  }
}
