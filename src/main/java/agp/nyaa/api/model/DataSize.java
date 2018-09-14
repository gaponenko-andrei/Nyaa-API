package agp.nyaa.api.model;

import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

import static com.google.common.base.Preconditions.checkArgument;
import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;

@Immutable(builder = false)
@Style(visibility = PACKAGE)
public abstract class DataSize {

  @Value.Parameter
  public abstract Float value();

  @Value.Parameter
  public abstract Unit unit();


  public static DataSize of(final Float value, final Unit unit) {
    validate(value);
    return ImmutableDataSize.of(value, unit);
  }

  private static void validate(final Float value) {
    checkArgument(isValid(value), "Provided data size value of %s is less then zero.", value);
  }

  private static boolean isValid(final Float value) {
    return value > 0;
  }

  public enum Unit {
    BYTE,
    KILOBYTE,
    MEGABYTE,
    GIGABYTE,
    TERABYTE
  }
}
