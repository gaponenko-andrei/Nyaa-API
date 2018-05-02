package agp.nyaa.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Iterator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestCases {

  public static Iterator<Object[]> from(final Collection<?> testCases) {
    return testCases.stream().map(a -> new Object[]{a}).iterator();
  }
}
