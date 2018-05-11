package agp.nyaa.api.test;

import agp.nyaa.api.mapper.DataSizeUnitMapper;
import agp.nyaa.api.model.DataSize;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.val;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public interface TestDataSizeUnitMapper extends DataSizeUnitMapper {

  static TestDataSizeUnitMapper to(@NonNull final DataSize.Unit unit) {
    return new TestDataSizeUnitMapper() {

      @Override
      public ImmutableSet<String> supportedValues() {
        return ImmutableSet.of(unit.name());
      }

      @Override
      public DataSize.Unit apply(@NonNull final String unitName) {
        if (unit.name().equals(unitName)) {
          return unit;
        } else {
          throw new IllegalArgumentException();
        }
      }
    };
  }

  class ToRandomUnit implements DataSizeUnitMapper {

    @Override
    public ImmutableSet<String> supportedValues() {
      return ImmutableSet.of("Random");
    }

    @Override
    public DataSize.Unit apply(String s) {
      return DataSize.Unit.BYTE;
    }
  }

  class WithoutSupportedUnits implements TestDataSizeUnitMapper {

    @Override
    public ImmutableSet<String> supportedValues() {
      return ImmutableSet.of();
    }

    @Override
    public DataSize.Unit apply(String s) {
      return null;
    }
  }
}
