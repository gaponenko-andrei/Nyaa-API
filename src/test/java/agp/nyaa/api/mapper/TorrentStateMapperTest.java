package agp.nyaa.api.mapper;

import agp.nyaa.api.TestCases;
import agp.nyaa.api.test.TestCases;
import agp.nyaa.api.model.TorrentState;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.BeforeMethod;
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

  private TorrentStateMapper mapper;
  private String cssClass;
  private TorrentState mappingResult;

  @BeforeMethod
  public void setUp() {
    mapper = new TorrentStateMapper();
  }

  @Test(dataProvider = "cssClassTestCasesProvider")
  public void mapping(final String cssClass) {

    /* Arrange */
    givenCssClassIs(cssClass);

    /* Act */
    mapCssClass();

    /* Assert */
    assertExpectedResult();
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void throwsOnNullCssClass() {

    /* Arrange */
    givenCssClassIs(null);

    /* Act */
    mapCssClass();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnUnknownCssClass() {

    /* Arrange */
    givenCssClassIs("unknown");

    /* Act */
    mapCssClass();
  }

  private void givenCssClassIs(final String cssClass) {
    this.cssClass = cssClass;
  }

  private void mapCssClass() {
    this.mappingResult = mapper.map(cssClass);
  }

  private void assertExpectedResult() {
    assertEquals(mappingResult, getExpectedStateBy(cssClass));
  }

  @DataProvider(name = "cssClassTestCasesProvider")
  private static Iterator<Object[]> getCssClassTestCases() {
    return TestCases.from(MAPPING.keySet());
  }

  private static TorrentState getExpectedStateBy(final String cssClass) {
    return MAPPING.get(cssClass);
  }
}
