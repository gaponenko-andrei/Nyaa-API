package agp.nyaa.api.test;

import agp.nyaa.api.mapping.DataSizeUnitMapping;
import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableSet;

public class TestDataSizeUnitMapping implements DataSizeUnitMapping {

  private String unitString;
  private DataSize.Unit unit;

  public TestDataSizeUnitMapping from(final String unitString) {
    this.unitString = unitString;
    return this;
  }

  public TestDataSizeUnitMapping to(final DataSize.Unit unit) {
    this.unit = unit;
    return this;
  }

  @Override
  public ImmutableSet<String> supportedValues() {
    return (unitString == null)
      ? ImmutableSet.of()
      : ImmutableSet.of(unitString);
  }

  @Override
  public DataSize.Unit apply(final String unitString) {
    return unit;
  }
}
