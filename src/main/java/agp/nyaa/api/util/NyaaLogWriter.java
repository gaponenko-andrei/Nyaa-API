package agp.nyaa.api.util;

import static com.google.common.base.Throwables.getStackTraceAsString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
      logger.trace(getStackTraceAsString(exception));
    } else {
      logger.error(exception.getMessage());
    }
  }

  public void warnAbout(@NonNull final Exception exception) {
    if (logger.isTraceEnabled()) {
      logger.warn(getStackTraceAsString(exception));
    } else {
      logger.warn(exception.getMessage());
    }
  }
}
