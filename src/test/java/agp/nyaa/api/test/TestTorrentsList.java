package agp.nyaa.api.test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.TestException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static agp.nyaa.api.Constants.NYAA_SITE_BASE_URL;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TestTorrentsList {

  /* known test resources */
  private static final Path NON_EMPTY = Paths.get("./src/test/resources/torrents-list.html");
  private static final Path EMPTY = Paths.get("./src/test/resources/empty-torrents-list.html");

  /**
   * Path to related resource.
   */
  private final Path sourcePath;


  public static TestTorrentsList empty() {
    return new TestTorrentsList(EMPTY);
  }

  public static TestTorrentsList nonEmpty() {
    return new TestTorrentsList(NON_EMPTY);
  }

  public Elements getTorrentPreviewElements() {
    return this.get().select("tbody tr");
  }

  public Element get() {
    return this.asDocument().select("table.torrent-list").first();
  }

  private Document asDocument() {
    return Jsoup.parse(this.asHtmlString(), NYAA_SITE_BASE_URL);
  }

  private String asHtmlString() {
    try {
      return Files.asCharSource(sourcePath.toFile(), Charsets.UTF_8).read();
    } catch (IOException e) {
      throw new TestException(e);
    }
  }
}
