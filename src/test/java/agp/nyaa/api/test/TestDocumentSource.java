package agp.nyaa.api.test;

import agp.nyaa.api.source.ElementSource;
import com.google.common.io.Files;
import lombok.NonNull;
import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.TestException;

import java.io.IOException;
import java.nio.file.Path;

import static agp.nyaa.api.Constants.NYAA_SITE_BASE_URL;
import static com.google.common.base.Charsets.UTF_8;

public class TestDocumentSource implements ElementSource<Document> {

  @Override
  public Document getElementBy(@NonNull final Path path) {
    val htmlString = readHtmlStringFrom(path);
    return Jsoup.parse(htmlString, NYAA_SITE_BASE_URL);
  }

  private String readHtmlStringFrom(final Path resourcePath) {
    try {
      val resourceFile = resourcePath.toFile();
      return Files.asCharSource(resourceFile, UTF_8).read();
    } catch (IOException e) {
      throw new TestException(e);
    }
  }
}
