package agp.nyaa.api.mapper;

import agp.nyaa.api.model.TorrentState;
import agp.nyaa.api.test.TestCases;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static agp.nyaa.api.model.TorrentState.*;
import static org.testng.Assert.assertEquals;

public class TorrentStateMapperTest {

  private static final ImmutableMap<String, TorrentState> MAPPING =
    ImmutableMap.<String, TorrentState>builder()
      .put("default", NORMAL)
      .put("danger", REMAKE)
      .put("success", TRUSTED)
      .build();

  private TorrentStateMapper mapper = new TorrentStateMapper();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnNulls() {
    mapper.map(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnUnsupportedCssClasses() {
    mapper.map("unsupported");
  }

  @Test
  public void supportedValuesShouldMatchTestedValues() {
    assertEquals(mapper.supportedValues(), MAPPING.keySet());
  }

  @Test(dataProvider = "cssClassTestCasesProvider")
  public void mappingSupportedCssClassShouldProduceExpectedResult(final String cssClass) {

    // given
    final TorrentState actualMappingResult = mapper.map(cssClass);

    // Expect
    assertEquals(actualMappingResult, getExpectedStateBy(cssClass));
  }

  @DataProvider(name = "cssClassTestCasesProvider")
  private static Iterator<Object[]> getCssClassTestCases() {
    return TestCases.from(MAPPING.keySet());
  }

  private static TorrentState getExpectedStateBy(final String cssClass) {
    return MAPPING.get(cssClass);
  }
}
