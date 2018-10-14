package agp.nyaa.api.parsing;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

import agp.nyaa.api.element.Table;
import agp.nyaa.api.element.Tr;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.test.TestDocuments;
import agp.nyaa.api.test.TestTorrentPreviewParser;
import lombok.val;

// todo refactoring

public class TorrentsListTableParserTest {

  private final Table nonEmptyTorrentsList = newNonEmptyTorrentsList();
  private final Table emptyTorrentsList = newEmptyTorrentsList();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNullTorrentPreviewParser() {
    TorrentsListTableParser.using((TorrentPreviewTrParser) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNullElementArgument() {
    TorrentsListTableParser.using(newTorrentPreviewParserSpy()).parse((Table) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void throwsOnNonTorrentsListElement() throws IOException {
    TorrentsListTableParser.using(newTorrentPreviewParserSpy()).parse(newNonTorrentListTable());
  }

  private Table newNonTorrentListTable() {
    return selectFirstTableFrom("non-torrent-list-table.html");
  }

  /* Verify expected number of calls using torrent preview parser spy */

  @Test
  public void emptyTorrentsListParsing() {
    testCountOfParsedElementsIsExpected(emptyTorrentsList, 0);
  }

  @Test
  public void nonEmptyTorrentsListParsing() {
    testCountOfParsedElementsIsExpected(nonEmptyTorrentsList, 4);
  }

  private void testCountOfParsedElementsIsExpected(final Table torrentListTable,
                                                   final int expectedCountOfParsedElements) {

    /* Arrange */
    val parser = TorrentsListTableParser.using(newTorrentPreviewParserSpy());

     /* Act */
    parser.parse(torrentListTable);

    /* Assert */
    verifyExpectedCallsCountForTorrentPreviewParser(parser, expectedCountOfParsedElements);
  }

  /* Assert expected results when default torrent preview parser is used */

  @Test
  public void nonEmptyTorrentsListParsingUsingDefaultTorrentPreviewParser() {

    /* Arrange */
    val parser = TorrentsListTableParser.using(new TorrentPreviewTrParser());

    /* Act */
    val result = parser.parse(nonEmptyTorrentsList);

    /* Assert */
    assertEquals(result.ids(), ImmutableSet.of(1060623L, 1060486L, 1060485L, 1060483L));
  }

  /* Test Utility Methods */

  private Table newEmptyTorrentsList() {
    return selectFirstTableFrom("empty-torrent-list.html");
  }

  private Table newNonEmptyTorrentsList() {
    return selectFirstTableFrom("torrents-list-parser-test.html");
  }

  private Table selectFirstTableFrom(final String testDocumentName) {
    return TestDocuments.get(testDocumentName).select("table", Table::new).get(0);
  }

  private Parser<Tr, TorrentPreview> newTorrentPreviewParserSpy() {
    return spy(new TestTorrentPreviewParser());
  }

  private void verifyExpectedCallsCountForTorrentPreviewParser(
    final TorrentsListTableParser parser, final int expectedCallsCount) {

    verify(parser.getTorrentPreviewParser(), times(expectedCallsCount)).apply(any(Tr.class));
  }
}
