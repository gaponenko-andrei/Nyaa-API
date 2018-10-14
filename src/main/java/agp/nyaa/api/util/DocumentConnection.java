package agp.nyaa.api.util;

import com.google.common.base.Stopwatch;
import lombok.val;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.immutables.value.Value;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

import java.time.Duration;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;

@Value.Immutable
@Value.Style(visibility = PACKAGE, overshadowImplementation = true)
public abstract class DocumentConnection {

  public abstract String url();
  public abstract Optional<Predicate<Document>> filter();

  // alias
  public GetRs toNewGetRs() {
    return sendGetRq();
  }

  public GetRs sendGetRq() {
    val stopwatch = Stopwatch.createStarted();
    val responseBuilder = innerSendGetRq();
    val timeTaken = stopwatch.stop().elapsed();
    return responseBuilder.timeTaken(timeTaken).build();
  }

  private GetRs.Builder innerSendGetRq() {
    val connection = HttpConnection.connect(url());
    try {
      val document = connection.get();
      return newRsBuilderFor(document);
    } catch (Exception exception) {
      return newFailureRsBuilder(exception);
    }
  }

  private GetRs.Builder newRsBuilderFor(final Document document) {
    return isValid(document) ? newSuccessRsBuilderFor(document) : newFailureRsBuilder();
  }

  private boolean isValid(final Document document) {
    return filter().map(filter -> filter.test(document)).orElse(true);
  }

  private GetRs.Builder newFailureRsBuilder(final Exception exception) {
    return new GetRs.Builder().url(url()).failureMessage(exception.getMessage());
  }

  private GetRs.Builder newSuccessRsBuilderFor(final Document document) {
    return new GetRs.Builder().url(url()).document(document);
  }

  private GetRs.Builder newFailureRsBuilder() {
    return new GetRs.Builder().url(url()).failureMessage("Document doesn't match the filter");
  }

  static class Builder extends ImmutableDocumentConnection.Builder {}

  @Value.Immutable
  @Value.Style(visibility = PACKAGE, overshadowImplementation = true)
  public static abstract class GetRs {

    public abstract String url();
    public abstract Duration timeTaken();
    public abstract Optional<Document> document();
    public abstract Optional<String> failureMessage();


    /* orderings */

    public static Comparator<GetRs> bySuccessFirst() {
      return Comparator.comparing(GetRs::failure);
    }

    public static Comparator<GetRs> byTimeTaken() {
      return Comparator.comparing(GetRs::timeTaken);
    }

    /* calculated fields */

    public final Boolean success() {
      return document().isPresent();
    }

    public final Boolean failure() {
      return !success();
    }

    @Override
    public String toString() {
      val toStringBuilder = new ToStringBuilder(this)
        .append("url", url())
        .append(" success", success())
        .append(" timeTaken", timeTaken());

      if (this.failure() && failureMessage().isPresent()) {
        toStringBuilder.append(" failureMessage", failureMessage().get());
      }

      return toStringBuilder.build();
    }

    static class Builder extends ImmutableGetRs.Builder { }
  }
}
