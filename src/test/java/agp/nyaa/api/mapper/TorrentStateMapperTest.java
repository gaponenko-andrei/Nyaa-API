package agp.nyaa.api.mapper;

import agp.nyaa.api.model.TorrentState;
import com.google.common.collect.ImmutableBiMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import properties.Constants;

import java.util.stream.Stream;

import static agp.nyaa.api.model.TorrentState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TorrentStateMapperTest {

  private static final ImmutableBiMap<String, TorrentState> CLASS_NAME_TO_INSTANCE_MAP =
          ImmutableBiMap.<String, TorrentState>
                  builder()
                  .put(Constants.torrent.state.normal.className, NORMAL)
                  .put(Constants.torrent.state.danger.className, REMAKE)
                  .put(Constants.torrent.state.trusted.className, TRUSTED)
                  .build();

  //
  // TorrentState -> CSS Class
  //

  @ParameterizedTest
  @EnumSource(TorrentState.class)
  void toCssClassMapping(final TorrentState torrentState) {

    /* Arrange */
    final Mapper<TorrentState, String> mapper = TorrentStateMapper.toCssClass();

    /* Act */
    final String mappingResult = mapper.map(torrentState);

    /* Assert */
    assertEquals(getClassNameBy(torrentState), mappingResult);
  }

  @Test
  void toCssClassMapperThrowsOnNullArgument() {
    assertThrows(NullPointerException.class,
            () -> TorrentStateMapper.toCssClass().map(null));
  }

  private static String getClassNameBy(final TorrentState torrentState) {
    return CLASS_NAME_TO_INSTANCE_MAP.inverse().get(torrentState);
  }

  //
  // CSS Class -> TorrentState
  //

  @ParameterizedTest
  @MethodSource("cssClassesProvider")
  void toTorrentStateMapping(final String cssClass) {

    /* Arrange */
    final Mapper<String, TorrentState> mapper = TorrentStateMapper.fromCssClass();

    /* Act */
    final TorrentState mappingResult = mapper.map(cssClass);

    /* Assert */
    assertEquals(getTorrentStateBy(cssClass), mappingResult);
  }

  @Test
  void toTorrentStateMapperThrowsOnNullArgument() {
    assertThrows(NullPointerException.class,
            () -> TorrentStateMapper.fromCssClass().map(null));
  }

  @Test
  void toTorrentStateMapperThrowsOnUnknownArgument() {
    assertThrows(IllegalArgumentException.class,
            () -> TorrentStateMapper.fromCssClass().map("unknown_css_class"));
  }

  private static Stream<String> cssClassesProvider() {
    return CLASS_NAME_TO_INSTANCE_MAP.keySet().stream();
  }

  private static TorrentState getTorrentStateBy(final String cssClass) {
    return CLASS_NAME_TO_INSTANCE_MAP.get(cssClass);
  }
}
