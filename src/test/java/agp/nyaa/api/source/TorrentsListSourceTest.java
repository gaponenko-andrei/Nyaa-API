package agp.nyaa.api.source;

import agp.nyaa.api.test.TestDocumentSource;
import agp.nyaa.api.test.TestResources;
import lombok.val;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


public class TorrentsListSourceTest {

  private final TorrentsListSource torrentsListSource =
    TorrentsListSource.filtering(new TestDocumentSource());


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void constructionThrowsOnNullDocumentSource() {
    TorrentsListSource.filtering((ElementSource<Document>) null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getElementByPathThrowsOnDocumentWithoutTorrentListTable() {
    getElementByResourceName("document-without-torrent-list-table.html");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getElementByPathThrowsOnDocumentWithMoreThanOneTorrentListTable() {
    getElementByResourceName("document-with-two-torrent-list-tables.html");
  }

  @Test
  public void getElementByPathReturnsTorrentListTableElementForValidTorrentListDocument() {
    val element = getElementByResourceName("empty-torrents-list.html");
    assertTrue(element.is("table.torrent-list"));
  }

  private Element getElementByResourceName(final String resourceName) {
    val documentPath = TestResources.get(resourceName);
    return torrentsListSource.getElementBy(documentPath);
  }
}
