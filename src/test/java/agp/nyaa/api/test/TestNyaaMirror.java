package agp.nyaa.api.test;

import java.net.URL;

import agp.nyaa.api.mirror.NyaaMirror;
import agp.nyaa.api.mirror.UnverifiedNyaaMirror;
import agp.nyaa.api.util.DocumentConnection;
import agp.nyaa.api.util.Try;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true, chain = true)
public class TestNyaaMirror implements NyaaMirror {

  private final URL url;
  private final DocumentConnection connection;


  public TestNyaaMirror(final String url, final DocumentConnection connection) {
    this.url = Try.call(() -> new URL(url)).result();
    this.connection = connection;
  }

  public UnverifiedNyaaMirror unverified() {
    return new UnverifiedNyaaMirror() {

      @Override
      public URL url() {
        return url;
      }

      @Override
      public DocumentConnection connection() {
        return connection;
      }
    };
  }
}
