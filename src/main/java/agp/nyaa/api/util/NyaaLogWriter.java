package agp.nyaa.api.util;

import com.google.common.base.Throwables;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor(access = AccessLevel.MODULE)
public final class NyaaLogWriter {

  @NonNull
  private final Logger logger;

  public static NyaaLogWriter of(final Class<?> loggedClass) {
    return new NyaaLogWriter(loggedClass);
  }

  private NyaaLogWriter(final Class<?> loggedClass) {
    logger = LoggerFactory.getLogger(loggedClass);
  }

  public void log(@NonNull final Exception exception) {
    if (logger.isTraceEnabled()) {
      logStackTraceOf(exception);
    } else {
      logErrorMessageOf(exception);
    }
  }

  private void logStackTraceOf(final Exception exception) {
    val stackTraceMessage = Throwables.getStackTraceAsString(exception);
    logger.trace(stackTraceMessage);
  }

  private void logErrorMessageOf(final Exception exception) {
    val errorMessage = exception.getMessage();
    logger.error(errorMessage);
  }
}
