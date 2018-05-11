package agp.nyaa.api.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class DataSize {

  private int value;
  private Unit unit;

  @RequiredArgsConstructor
  public enum Unit {
    BYTE,
    KILOBYTE,
    MEGABYTE,
    GIGABYTE,
    TERABYTE
  }
}
