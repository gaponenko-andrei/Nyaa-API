package agp.nyaa.api.test;

import agp.nyaa.api.mapper.DataSizeUnitMapper;
import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableSet;

public interface TestDataSizeUnitMapper extends DataSizeUnitMapper {

  static TestDataSizeUnitMapper.Impl from(final String unitString) {
    return new TestDataSizeUnitMapper.Impl().from(unitString);
  }

  class Impl implements TestDataSizeUnitMapper {

    private String unitString;
    private DataSize.Unit unit;

    public TestDataSizeUnitMapper.Impl from(final String unitString) {
      this.unitString = unitString;
      return this;
    }

    public TestDataSizeUnitMapper.Impl to(final DataSize.Unit unit) {
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
}
