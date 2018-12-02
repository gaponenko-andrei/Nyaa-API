package agp.nyaa.api.mapping;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Suppliers.memoize;
import static java.lang.String.format;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import agp.nyaa.api.model.DataSize;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

public final class DataSizeMapping implements Function<String, DataSize> {

  @Getter
  @NonNull
  private final DataSizeUnitMapping unitMapping;

  @NonNull
  private final Supplier<Pattern> dataSizePattern = memoize(this::newDataSizePattern);


  public static DataSizeMapping using(@NonNull final DataSizeUnitMapping dataSizeUnitMapping) {
    return new DataSizeMapping(dataSizeUnitMapping);
  }

  private DataSizeMapping(@NonNull final DataSizeUnitMapping dataSizeUnitMapping) {
    validate(dataSizeUnitMapping);
    this.unitMapping = dataSizeUnitMapping;
  }

  @Override
  public DataSize apply(@NonNull final String dataSize) {
    validate(dataSize);
    val dataSizeValue = extractSizeValueOf(dataSize);
    val dataSizeUnit = extractUnitValueOf(dataSize);
    return DataSize.of(dataSizeValue, dataSizeUnit);
  }

  private void validate(final DataSizeUnitMapping dataSizeUnitMapping) {
    checkArgument(
      isValid(dataSizeUnitMapping),
      "Argument 'unitMapping' must have more then 0 supported values."
    );
  }

  private void validate(final String dataSize) {
    checkArgument(
      isValid(dataSize),
      "Argument 'dataSize' must match the following " +
        "regex: %s, but provided string value of [%s] fails " +
        "to do so.", dataSizePattern.get().pattern(), dataSize
    );
  }

  private boolean isValid(final DataSizeUnitMapping dataSizeUnitMapping) {
    return dataSizeUnitMapping.supportedValues().size() > 0;
  }

  private boolean isValid(final String dataSize) {
    return dataSize.matches(dataSizePattern.get().pattern());
  }

  private Float extractSizeValueOf(final String dataSize) {
    val stringValue = substringSizeOf(dataSize);
    return Float.valueOf(stringValue);
  }

  private DataSize.Unit extractUnitValueOf(final String dataSize) {
    val stringValue = substringUnitOf(dataSize);
    return unitMapping.apply(stringValue);
  }

  private String substringSizeOf(final String dataSize) {
    val matcher = dataSizePattern.get().matcher(dataSize);

    if (matcher.find()) {
      return matcher.group(1);
    } else {
      throw new IllegalStateException(format(
        "Failed to find size value group of 'dataSize' string [%s].", dataSize
      ));
    }
  }

  private String substringUnitOf(final String dataSize) {
    val matcher = dataSizePattern.get().matcher(dataSize);

    if (matcher.find()) {
      return matcher.group(2);
    } else {
      throw new IllegalStateException(format(
        "Failed to find unit group of 'dataSize' string [%s].", dataSize
      ));
    }
  }

  private Pattern newDataSizePattern() {
    val values = "([1-9][0-9]*[/.0-9]*)";
    val spaces = "\\s*";
    val units = newSupportedUnitsGroupRegex();
    val dataSizeRegex = spaces + values + spaces + units + spaces;
    return Pattern.compile(dataSizeRegex);
  }

  private String newSupportedUnitsGroupRegex() {
    val supportedDataSizeUnits = unitMapping.supportedValues();

    if (supportedDataSizeUnits.isEmpty()) {
      return "";
    } else {
      return format("(%s)", on("|").join(supportedDataSizeUnits));
    }
  }
}
