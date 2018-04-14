package extensions.org.jsoup.nodes.Element;

import agp.nyaa.api.test.HtmlResource;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import properties.Constants;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ElementExtensionTest {

  private static final String TORRENT_LIST_SELECTOR = "." + Constants.torrent.list.className;

  @Test
  void isTorrentsListThrowsOnNull() {
    assertThrows(NullPointerException.class,
            () -> ((Element) null).isTorrentList());
  }

  @ParameterizedTest
  @MethodSource("torrentListsProvider")
  void isTorrentsListTrueCase(final Element element) {
    assertTrue(element.isTorrentList());
  }

  @ParameterizedTest
  @MethodSource("nonTorrentListsProvider")
  void isTorrentsListFalseCase(final Element element) {
    assertFalse(element.isTorrentList());
  }

  private static Stream<Element> torrentListsProvider() throws IOException {
    final Document document = HtmlResource.EMPTY_TORRENT_LIST.asDocument();
    final Element emptyTorrentList = document.selectFirst(TORRENT_LIST_SELECTOR);
    return Stream.of(emptyTorrentList);
  }

  private static Stream<Element> nonTorrentListsProvider() throws IOException {
    return Stream.of(HtmlResource.TORRENT_PREVIEW_ITEM.asDocument());
  }
}
