package agp.nyaa.api.parsing;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

import agp.nyaa.api.element.Table;
import agp.nyaa.api.test.TestDocuments;
import lombok.val;


public class TorrentListTableParserTest {

  private final Table nonEmptyTorrentListTable = newNonEmptyTorrentListTable();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void usingNullTorrentPreviewParserShouldThrow() {
    TorrentListTableParser.using((TorrentPreviewTrParser) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void parsingShouldThrowOnNulls() {
    TorrentListTableParser.using(new TorrentPreviewTrParser()).parse((Table) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void parsingShouldThrowOnNonTorrentListTables() throws IOException {
    TorrentListTableParser.using(new TorrentPreviewTrParser()).parse(newNonTorrentListTable());
  }

  @Test
  public void parserUsingDefaultTorrentPreviewParserShouldProduceExpectedResult() {

    // given
    val parser = TorrentListTableParser.using(new TorrentPreviewTrParser());

    // when
    val result = parser.parse(nonEmptyTorrentListTable);

    // then
    assertEquals(result.ids(), ImmutableSet.of(1060623L, 1060486L, 1060485L, 1060483L));
  }

  /* Utils */

  private Table newNonEmptyTorrentListTable() {
    return selectFirstTableFrom("torrents-list-parser-test.html");
  }

  private Table newNonTorrentListTable() {
    return selectFirstTableFrom("non-torrent-list-table.html");
  }

  private Table selectFirstTableFrom(final String testDocumentName) {
    return TestDocuments.get(testDocumentName).select("table", Table::new).get(0);
  }
}
