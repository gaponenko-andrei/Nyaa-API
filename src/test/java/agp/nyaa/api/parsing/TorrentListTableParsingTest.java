package agp.nyaa.api.parsing;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

import agp.nyaa.api.element.Table;
import agp.nyaa.api.test.TestDocuments;
import lombok.val;


public class TorrentListTableParsingTest {

  private final Table nonEmptyTorrentListTable = newNonEmptyTorrentListTable();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void usingNullTorrentPreviewParsingShouldThrow() {
    TorrentListTableParsing.using((TorrentPreviewTrParsing) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void parsingShouldThrowOnNull() {
    TorrentListTableParsing.using(new TorrentPreviewTrParsing()).apply((Table) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void parsingShouldThrowOnNonTorrentListTables() throws IOException {
    TorrentListTableParsing.using(new TorrentPreviewTrParsing()).apply(newNonTorrentListTable());
  }

  @Test
  public void parsingUsingDefaultTorrentPreviewParsingShouldProduceExpectedResult() {

    // given
    val parsing = TorrentListTableParsing.using(new TorrentPreviewTrParsing());

    // when
    val result = parsing.apply(nonEmptyTorrentListTable);

    // then
    assertEquals(result.ids(), ImmutableSet.of(1060623L, 1060486L, 1060485L, 1060483L));
  }

  /* Utils */

  private Table newNonEmptyTorrentListTable() {
    return selectFirstTableFrom("torrents-list-parsing-test.html");
  }

  private Table newNonTorrentListTable() {
    return selectFirstTableFrom("non-torrent-list-table.html");
  }

  private Table selectFirstTableFrom(final String testDocumentName) {
    return TestDocuments.get(testDocumentName).select("table", Table::new).get(0);
  }
}
