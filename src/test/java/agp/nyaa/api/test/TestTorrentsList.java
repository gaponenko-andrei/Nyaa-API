package agp.nyaa.api.test;

import lombok.NonNull;
import lombok.val;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.google.common.base.Verify.verify;
import static com.google.common.base.Verify.verifyNotNull;

public class TestTorrentsList {

  private final Element listElement;

  public static TestTorrentsList of(@NonNull final Document listDocument) {
    return new TestTorrentsList(listDocument);
  }

  public Elements selectTorrentPreviewTrs() {
    return this.get().select("tbody tr");
  }

  public Element get() {
    return listElement;
  }

  private TestTorrentsList(final Document listDocument) {
    val listTables = listDocument.select("table.torrent-list");
    verify(listTables.size() == 1);

    val firstListTable = listTables.first();
    this.listElement = verifyNotNull(firstListTable);
  }
}
