package agp.nyaa.api.test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import properties.Constants;

import java.io.File;
import java.io.IOException;

public enum HtmlResource {

  TORRENT_PREVIEW_ITEM("./src/test/resources/torrent-preview-item.html"),
  EMPTY_TORRENT_LIST("./src/test/resources/empty-torrent-list.html");

  private final String path;

  HtmlResource(final String path) {
    this.path = path;
  }

  public String asString() throws IOException {
    return Files.asCharSource(new File(path), Charsets.UTF_8).read();
  }

  public Document asDocument() throws IOException {
    return Jsoup.parse(this.asString(), Constants.nyaa.si.baseUri);
  }

  public Element asDocumentBody() throws IOException {
    return this.asDocument().body();
  }
}
