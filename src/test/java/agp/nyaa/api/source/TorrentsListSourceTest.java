package agp.nyaa.api.source;

import agp.nyaa.api.test.TestDocumentSource;
import lombok.val;
import org.jsoup.nodes.Document;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


public class TorrentsListSourceTest {

  private final TorrentsListSource torrentsListSource =
    TorrentsListSource.basedOn(new TestDocumentSource());


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructionThrowsOnNullDocumentSource() {
    TorrentsListSource.basedOn((ElementSource<Document>) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getElementByPathThrowsOnDocumentWithoutTorrentListTable() {
    torrentsListSource.get("document-without-torrent-list-table.html");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getElementByPathThrowsOnDocumentWithMoreThanOneTorrentListTable() {
    torrentsListSource.get("document-with-two-torrent-list-tables.html");
  }

  @Test
  public void getElementByPathReturnsTorrentListTableElementForValidTorrentListDocument() {
    val element = torrentsListSource.get("empty-torrents-list.html");
    assertTrue(element.is("table.torrent-list"));
  }
}
