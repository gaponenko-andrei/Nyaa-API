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


  @Test(dataProvider = "cssClassTestCasesProvider")
  public void mapping(final String cssClass) {

    /* Act */
    final TorrentState actualMappingResult = mapper.map(cssClass);

    /* Assert */
    assertEquals(actualMappingResult, getExpectedStateBy(cssClass));
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void throwsOnNullCssClass() {
    mapper.map(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnUnknownCssClass() {
    mapper.map("unknown");
  }

  @Test
  public void supportedValues() {
    assertEquals(mapper.supportedValues(), MAPPING.keySet());
  }

  @DataProvider(name = "cssClassTestCasesProvider")
  private static Iterator<Object[]> getCssClassTestCases() {
    return TestCases.from(MAPPING.keySet());
  }

  private static TorrentState getExpectedStateBy(final String cssClass) {
    return MAPPING.get(cssClass);
  }
}
