package agp.nyaa.api.mapper;

import agp.nyaa.api.TestCases;
import agp.nyaa.api.model.TorrentState;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static agp.nyaa.api.model.TorrentState.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class TorrentStateMapperTest {

  private static final ImmutableMap<String, TorrentState> MAPPING =
          ImmutableMap.<String, TorrentState>builder()
                  .put("default", NORMAL)
                  .put("danger", REMAKE)
                  .put("success", TRUSTED)
                  .build();

  private final TorrentStateMapper<String, TorrentState> mapper = TorrentStateMapper.fromCssClass();


  @Test(dataProvider = "cssClassesProvider")
  public void toTorrentStateMapping(final String cssClass) {

    /* Act */
    val mappingResult = mapper.map(cssClass);

    /* Assert */
    assertEquals(mappingResult, getTorrentStateBy(cssClass));
  }

  @Test
  public void toTorrentStateMapperThrowsOnNullArgument() {
    assertThrows(NullPointerException.class, () -> mapper.map(null));
  }

  @Test
  public void toTorrentStateMapperThrowsOnUnknownArgument() {
    assertThrows(IllegalArgumentException.class, () -> mapper.map("unknown_css_class"));
  }

  @DataProvider(name = "cssClassesProvider")
  private static Iterator<Object[]> getTestCssClasses() {
    return TestCases.from(MAPPING.keySet());
  }

  private static TorrentState getTorrentStateBy(final String cssClass) {
    return MAPPING.get(cssClass);
  }
}
