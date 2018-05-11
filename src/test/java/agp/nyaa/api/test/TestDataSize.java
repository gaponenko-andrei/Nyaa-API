package agp.nyaa.api.test;

import agp.nyaa.api.mapper.DataSizeMapper;
import agp.nyaa.api.mapper.DataSizeUnitMapper;
import agp.nyaa.api.model.DataSize.Unit;
import lombok.experimental.Accessors;
import lombok.val;

import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;

@Accessors(fluent = true)
public class TestDataSize {

  private String value;
  private String unit;

  public static TestDataSize from(final Integer value, final Unit unit) {
    return new TestDataSize().withValue(value).withUnit(unit);
  }

  public TestDataSize withValue(final Integer value) {
    this.value = String.valueOf(value);
    return this;
  }

  public TestDataSize withRandomValue() {
    val randomInteger = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    this.value = String.valueOf(randomInteger);
    return this;
  }

  public TestDataSize withoutValue() {
    this.value = "";
    return this;
  }

  public TestDataSize withUnit(final String unit) {
    this.unit = unit;
    return this;
  }

  public TestDataSize withUnit(final Unit unit) {
    this.unit = unit.name();
    return this;
  }

  public TestDataSize withUnitSupportedBy(final DataSizeMapper dataSizeMapper) {
    return withUnitSupportedBy(dataSizeMapper.getUnitMapper());
  }

  public TestDataSize withUnitSupportedBy(final DataSizeUnitMapper dataSizeUnitMapper) {
    checkArgument(!dataSizeUnitMapper.supportedValues().isEmpty());
    this.unit = dataSizeUnitMapper.supportedValues().asList().get(0);
    return this;
  }

  public TestDataSize withoutUnit() {
    this.unit = "";
    return this;
  }

  @Override
  public String toString() {
    return toFormattedStringBy("%s %s");
  }

  public String toFormattedStringBy(final String template) {
    return String.format(template, value, unit);
  }
}
