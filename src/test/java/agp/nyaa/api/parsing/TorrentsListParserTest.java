package agp.nyaa.api.parsing;

import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.source.ElementSource;
import agp.nyaa.api.source.TorrentsListSource;
import agp.nyaa.api.test.TestDocumentSource;
import agp.nyaa.api.test.TestResources;
import agp.nyaa.api.test.TestTorrentPreviewParser;
import com.google.common.collect.ImmutableSet;
import lombok.val;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class TorrentsListParserTest {

  private final ElementSource torrentsListSource = TorrentsListSource.basedOn(new TestDocumentSource());
  private final Element nonEmptyTorrentsList = newNonEmptyTorrentsList();
  private final Element emptyTorrentsList = newEmptyTorrentsList();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNullTorrentPreviewParser() {
    TorrentsListParser.using((TorrentPreviewParser) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNullElementArgument() {
    TorrentsListParser.using(newTorrentPreviewParserSpy()).parse((Element) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNonTorrentsListElement() throws IOException {
    TorrentsListParser.using(newTorrentPreviewParserSpy()).parse(newNonTorrentsListElement());
  }

  private Element newNonTorrentsListElement() {
    return nonEmptyTorrentsList.select("tr").first();
  }

  /* Test to verify expected number of calls using torrent preview parser spy */

  @Test
  public void emptyTorrentsListParsing() {
    testCountOfParsedElementsIsExpected(emptyTorrentsList, 0);
  }

  @Test
  public void nonEmptyTorrentsListParsing() {
    testCountOfParsedElementsIsExpected(nonEmptyTorrentsList, 4);
  }

  private void testCountOfParsedElementsIsExpected(final Element testTorrentList,
                                                   final int expectedCountOfParsedElements) {

    /* Arrange */
    val parser = TorrentsListParser.using(newTorrentPreviewParserSpy());

     /* Act */
    parser.parse(testTorrentList);

    /* Assert */
    verifyExpectedCallsCountForTorrentPreviewParser(parser, expectedCountOfParsedElements);
  }

  /* Tests to assert expected results when default torrent preview parser is used */

  @Test
  public void nonEmptyTorrentsListParsingUsingDefaultTorrentPreviewParser() {

    /* Arrange */
    val parser = TorrentsListParser.using(new TorrentPreviewParser());

    /* Act */
    val result = parser.parse(nonEmptyTorrentsList);

    /* Assert */
    assertEquals(result.ids(), ImmutableSet.of(1060623L, 1060486L, 1060485L, 1060483L));
  }

  /* Test Utility Methods */

  private Element newEmptyTorrentsList() {
    return newTestTorrentsListFrom("empty-torrents-list.html");
  }

  private Element newNonEmptyTorrentsList() {
    return newTestTorrentsListFrom("torrents-list-parser-test.html");
  }

  private Element newTestTorrentsListFrom(final String fileName) {
    val listDocumentPath = TestResources.get(fileName);
    return torrentsListSource.getElementBy(listDocumentPath);
  }

  private Parser<Element, TorrentPreview> newTorrentPreviewParserSpy() {
    return spy(new TestTorrentPreviewParser());
  }

  private void verifyExpectedCallsCountForTorrentPreviewParser(
    final TorrentsListParser parser, final int expectedCallsCount) {

    verify(parser.getTorrentPreviewParser(), times(expectedCallsCount)).apply(any(Element.class));
  }
}
