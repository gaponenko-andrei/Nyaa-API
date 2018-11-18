package agp.nyaa.api.util;

import static com.google.common.base.Throwables.getStackTraceAsString;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Iterator;

import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import agp.nyaa.api.test.TestCases;

public class NyaaLogWriterTest {

  private Exception exception;
  private Logger wrappedLogger;
  private NyaaLogWriter nyaaLogWriter;

  @BeforeMethod
  public void setUp() {
    exception = new NullPointerException("test exception");
    wrappedLogger = mock(Logger.class);
    nyaaLogWriter = new NyaaLogWriter(wrappedLogger);
  }

  @Test
  public void loggingExceptionWhenTraceEnabledShouldMakeSingleTraceCall() {

    // given
    givenTraceEnabled();

    // when
    nyaaLogWriter.log(exception);

    // then
    verify(wrappedLogger, times(1)).trace(anyString());
  }

  @Test
  public void loggingExceptionWhenTraceEnabledShouldMakeOnlyTraceCall() {

    // given
    givenTraceEnabled();

    // when
    nyaaLogWriter.log(exception);

    // then
    verifyOnlyTraceCall();
  }

  @Test
  public void loggedExceptionStackShouldBeTracedWhenTraceEnabled() {

    // given
    givenTraceEnabled();

    // when
    nyaaLogWriter.log(exception);

    // then
    verify(wrappedLogger).trace(getStackTraceAsString(exception));
  }

  @Test
  public void loggingExceptionWhenTraceDisabledShouldMakeZeroTraceCalls() {

    // given
    givenTraceDisabled();

    // when
    nyaaLogWriter.log(exception);

    // then
    verify(wrappedLogger, times(0)).trace(anyString());
  }

  @Test
  public void loggingExceptionWhenTraceDisabledShouldMakeErrorCallInstead() {

    // given
    givenTraceDisabled();

    // when
    nyaaLogWriter.log(exception);

    // then
    verify(wrappedLogger, times(1)).error(anyString());
  }

  @Test
  public void loggingExceptionWhenTraceDisabledShouldMakeOnlyErrorCall() {

    // given
    givenTraceDisabled();

    // when
    nyaaLogWriter.log(exception);

    // then
    verifyOnlyErrorCall();
  }

  @Test
  public void loggedExceptionMessageShouldBeWrittenAsErrorWhenTraceDisabled() {

    // given
    givenTraceDisabled();

    // when
    nyaaLogWriter.log(exception);

    // then
    verify(wrappedLogger).error(exception.getMessage());
  }

  @Test(dataProvider = "traceEnabledTestCasesProvider",
    expectedExceptions = IllegalArgumentException.class)
  public void loggingShouldThrowOnNull(final boolean traceEnabledValue) {

    // given
    givenTraceEnabledIs(traceEnabledValue);

    // when
    nyaaLogWriter.log(null);
  }

  @Test
  public void warnAboutShouldWriteExceptionStackTraceWhenTraceEnabled() {

    // given
    givenTraceEnabled();

    // when
    nyaaLogWriter.warnAbout(exception);

    // then
    verify(wrappedLogger).warn(getStackTraceAsString(exception));
  }

  @Test
  public void warnAboutShouldWriteExceptionMessageWhenTraceDisabled() {

    // given
    givenTraceDisabled();

    // when
    nyaaLogWriter.warnAbout(exception);

    // then
    verify(wrappedLogger).warn(exception.getMessage());
  }

  @Test(dataProvider = "traceEnabledTestCasesProvider")
  public void warnAboutShouldMakeOnlyWarnCall(final boolean traceEnabled) {

    // given
    givenTraceEnabledIs(traceEnabled);

    // when
    nyaaLogWriter.warnAbout(exception);

    // then
    verifyOnlyWarnCall();
  }

  /* utils */

  private void givenTraceEnabled() {
    givenTraceEnabledIs(true);
  }

  private void givenTraceDisabled() {
    givenTraceEnabledIs(false);
  }

  private void givenTraceEnabledIs(final boolean traceIsEnabled) {
    when(wrappedLogger.isTraceEnabled()).thenReturn(traceIsEnabled);
  }

  private void verifyOnlyTraceCall() {
    verify(wrappedLogger).isTraceEnabled();
    verify(wrappedLogger).trace(anyString());
    verifyNoMoreInteractions(wrappedLogger);
  }

  private void verifyOnlyErrorCall() {
    verify(wrappedLogger).isTraceEnabled();
    verify(wrappedLogger).error(anyString());
    verifyNoMoreInteractions(wrappedLogger);
  }

  private void verifyOnlyWarnCall() {
    verify(wrappedLogger).isTraceEnabled();
    verify(wrappedLogger).warn(anyString());
    verifyNoMoreInteractions(wrappedLogger);
  }

  @DataProvider(name = "traceEnabledTestCasesProvider")
  private static Iterator<Object[]> getTraceEnabledTestCases() {
    return TestCases.forBoolean();
  }
}
