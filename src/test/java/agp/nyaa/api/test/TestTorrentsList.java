package agp.nyaa.api.test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.NonNull;
import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.TestException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static agp.nyaa.api.Constants.NYAA_SITE_BASE_URL;
import static com.google.common.base.Verify.verify;
import static com.google.common.base.Verify.verifyNotNull;

public class TestTorrentsList {

  private final Element listElement;


  public static TestTorrentsList empty() {
    return TestTorrentsList.fromResource("empty-torrents-list.html");
  }

  public static TestTorrentsList fromResource(@NonNull final String resourceName) {
    val resourcePath = Paths.get("./src/test/resources/").resolve(resourceName);
    return new TestTorrentsList(resourcePath);
  }

  public Elements selectTorrentPreviewTrs() {
    return this.get().select("tbody tr");
  }

  public Element get() {
    return listElement;
  }

  private TestTorrentsList(final Path resourcePath) {
    val listDocument = parseDocumentFrom(resourcePath);

    val listTables = listDocument.select("table.torrent-list");
    verify(listTables.size() == 1);

    val firstListTable = listTables.first();
    this.listElement = verifyNotNull(firstListTable);
  }

  private static Document parseDocumentFrom(final Path resourcePath) {
    val htmlString = readHtmlStringFrom(resourcePath);
    return Jsoup.parse(htmlString, NYAA_SITE_BASE_URL);
  }

  private static String readHtmlStringFrom(final Path resourcePath) {
    try {
      return Files.asCharSource(resourcePath.toFile(), Charsets.UTF_8).read();
    } catch (IOException e) {
      throw new TestException(e);
    }
  }
}
