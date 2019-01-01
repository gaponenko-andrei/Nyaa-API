package agp.nyaa.api.mirror;

import static java.lang.String.format;

import java.util.function.Function;

import agp.nyaa.api.util.NyaaLogWriter;
import lombok.NonNull;
import lombok.val;

public final class NyaaMirrorVerification implements Function<UnverifiedNyaaMirror, VerifiedNyaaMirror> {

  private static final NyaaLogWriter LOGGER = NyaaLogWriter.of(NyaaMirrorVerification.class);


  @Override
  public VerifiedNyaaMirror apply(@NonNull final UnverifiedNyaaMirror mirror) {
    val rs = mirror.connection().getRs().orElseThrow(cause -> newLoggedExceptionFor(mirror, cause));

    if (rs.document().has("table.torrent-list")) {
      return VerifiedNyaaMirror.aka(mirror.url());
    } else {
      throw newLoggedExceptionFor(mirror);
    }
  }

  private static Exception newLoggedExceptionFor(final UnverifiedNyaaMirror mirror, final Throwable cause) {
    return logAndReturn(new Exception(mirror, cause));
  }

  private static Exception newLoggedExceptionFor(final UnverifiedNyaaMirror mirror) {
    return logAndReturn(new Exception(mirror));
  }

  private static Exception logAndReturn(final Exception exception) {
    LOGGER.warnAbout(exception);
    return exception;
  }

  static final class Exception extends RuntimeException {

    Exception(@NonNull final UnverifiedNyaaMirror mirror) {
      super(format(
        "Verification failed for [%s], as it doesn't " +
          "point to 'table.torrent-list' document.", mirror)
      );
    }

    Exception(@NonNull final UnverifiedNyaaMirror mirror, @NonNull final Throwable cause) {
      super(format(
        "Verification failed for [%s] as unexpected " +
          "exception was thrown in the process.", mirror), cause
      );
    }
  }
}
