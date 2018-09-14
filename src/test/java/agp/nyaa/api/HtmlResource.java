package agp.nyaa.api;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

import static agp.nyaa.api.Constants.NYAA_SITE_BASE_URL;

public enum HtmlResource {

  TORRENTS_LIST("./src/test/resources/torrents-list.html"),
  EMPTY_TORRENTS_LIST("./src/test/resources/empty-torrents-list.html");

  private final String path;

  HtmlResource(final String path) {
    this.path = path;
  }

  public String asHtmlString() throws IOException {
    return Files.asCharSource(new File(path), Charsets.UTF_8).read();
  }

  public Document asDocument() throws IOException {
    return Jsoup.parse(this.asHtmlString(), NYAA_SITE_BASE_URL);
  }
}
