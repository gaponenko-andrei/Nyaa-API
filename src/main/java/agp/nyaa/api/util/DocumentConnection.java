package agp.nyaa.api.util;

import static agp.nyaa.api.util.Functions.*;
import static lombok.AccessLevel.PRIVATE;
import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;

import java.net.URL;
import java.time.Duration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.immutables.value.Value;
import org.jsoup.helper.HttpConnection;

import com.google.common.base.Stopwatch;

import agp.nyaa.api.element.ImmutableDocument;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;

@Value.Immutable(builder = false)
@Value.Style(
  of = "to",
  visibility = PACKAGE,
  overshadowImplementation = true
)
public abstract class DocumentConnection {

  @Value.Parameter
  public abstract URL url();


  public static DocumentConnection fromUrl(@NonNull final URL url) {
    return DocumentConnection.to(url);
  }

  public static DocumentConnection to(@NonNull final URL url) {
    return ImmutableDocumentConnection.to(url);
  }

  public Try<GetRs> getRs() {
    val stopwatch = Stopwatch.createStarted();
    val connection = HttpConnection.connect(url());

    return Try
      .call(connection::get)
      .map(as(ImmutableDocument::new))
      .map(document -> new GetRsImpl(stopwatch.elapsed(), document));
  }

  public interface GetRs {
    Duration timeTaken();
    ImmutableDocument document();
  }

  @Getter
  @Accessors(fluent = true)
  @RequiredArgsConstructor(access = PRIVATE)
  private final class GetRsImpl implements GetRs {

    @NonNull
    private final Duration timeTaken;

    @NonNull
    private final ImmutableDocument document;

    @Override
    public String toString() {
      return new ToStringBuilder(this)
        .append("url", url())
        .append(" timeTaken", timeTaken)
        .build();
    }
  }
}
