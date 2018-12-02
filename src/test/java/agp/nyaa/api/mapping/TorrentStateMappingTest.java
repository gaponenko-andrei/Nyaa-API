package agp.nyaa.api.mapping;

import agp.nyaa.api.model.TorrentState;
import agp.nyaa.api.test.TestCases;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static agp.nyaa.api.model.TorrentState.*;
import static org.testng.Assert.assertEquals;

public class TorrentStateMappingTest {

  private static final ImmutableMap<String, TorrentState> TESTED_VALUES =
    ImmutableMap.<String, TorrentState>builder()
      .put("default", NORMAL)
      .put("danger", REMAKE)
      .put("success", TRUSTED)
      .build();

  private TorrentStateMapping mapping = new TorrentStateMapping();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnNull() {
    mapping.apply(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnUnsupportedCssClass() {
    mapping.apply("unsupported");
  }

  @Test
  public void supportedValuesShouldMatchTestedValues() {
    assertEquals(mapping.supportedValues(), TESTED_VALUES.keySet());
  }

  @Test(dataProvider = "cssClassTestCasesProvider")
  public void mappingSupportedCssClassShouldProduceExpectedResult(final String cssClass) {

    // given
    final TorrentState actualMappingResult = mapping.apply(cssClass);

    // expect
    assertEquals(actualMappingResult, getExpectedStateBy(cssClass));
  }

  @DataProvider(name = "cssClassTestCasesProvider")
  private static Iterator<Object[]> getCssClassTestCases() {
    return TestCases.from(TESTED_VALUES.keySet());
  }

  private static TorrentState getExpectedStateBy(final String cssClass) {
    return TESTED_VALUES.get(cssClass);
  }
}
