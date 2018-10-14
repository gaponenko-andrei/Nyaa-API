package agp.nyaa.api.test;

import static com.google.common.base.Charsets.UTF_8;

import java.io.IOException;
import java.nio.file.Path;

import org.jsoup.Jsoup;
import org.testng.TestException;

import com.google.common.io.Files;

import agp.nyaa.api.element.ImmutableDocument;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class TestDocuments {

  public ImmutableDocument get(@NonNull final String fileName) {
    val resourcePath = TestResources.root().resolve(fileName);
    val htmlString = readHtmlStringFrom(resourcePath);
    val documentSource = Jsoup.parse(htmlString, "https://nyaa.si");
    return new ImmutableDocument(documentSource);
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
