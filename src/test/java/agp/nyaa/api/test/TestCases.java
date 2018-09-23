package agp.nyaa.api.test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestCases {

  public static Iterator<Object[]> from(final Collection<?> testCases) {
    return testCases.stream().map(a -> new Object[]{a}).iterator();
  }

  public static Iterator<Object[]> forBoolean() {
    return TestCases.from(Arrays.asList(true, false));
  }
}
