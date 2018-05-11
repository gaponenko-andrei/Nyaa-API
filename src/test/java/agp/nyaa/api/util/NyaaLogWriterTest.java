package agp.nyaa.api.util;

import agp.nyaa.api.test.TestCases;
import com.google.common.base.Throwables;
import lombok.val;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class NyaaLogWriterTest {

  private Exception exception;
  private Logger wrappedLogger;
  private NyaaLogWriter nyaaLogWriter;

  @BeforeMethod
  public void setUp() {
    exception = null;
    wrappedLogger = mock(Logger.class);
    nyaaLogWriter = new NyaaLogWriter(wrappedLogger);
  }

  @Test
  public void loggingExceptionCallsTraceWhenTraceIsEnabled() {

    /* Arrange */
    givenException();
    givenTraceEnabledIs(true);

    /* Act */
    logException();

    /* Assert */
    verifySingleTraceCall();
  }

  @Test
  public void loggingExceptionCallsNothingButTraceWhenTraceIsEnabled() {

    /* Arrange */
    givenException();
    givenTraceEnabledIs(true);

    /* Act */
    logException();

    /* Assert */
    verifyNothingButTrace();
  }

  @Test
  public void loggingExceptionWritesExceptionStackTraceWhenTraceIsEnabled() {

    /* Arrange */
    givenException();
    givenTraceEnabledIs(true);

    /* Act */
    logException();

    /* Assert */
    verifyExceptionStackWasTraced();
  }

  @Test
  public void loggingExceptionDoesNotCallTraceWhenTraceIsDisabled() {

    /* Arrange */
    givenException();
    givenTraceEnabledIs(false);

    /* Act */
    logException();

    /* Assert */
    verifyZeroTraceCalls();
  }

  @Test
  public void loggingExceptionCallsErrorWhenTraceIsDisabled() {

    /* Arrange */
    givenException();
    givenTraceEnabledIs(false);

    /* Act */
    logException();

    /* Assert */
    verifySingleErrorCall();
  }

  @Test
  public void loggingExceptionCallsNothingButErrorWhenTraceIsDisabled() {

    /* Arrange */
    givenException();
    givenTraceEnabledIs(false);

    /* Act */
    logException();

    /* Assert */
    verifyNothingButError();
  }

  @Test
  public void loggingExceptionWritesExceptionMessageWhenTraceIsDisabled() {

    /* Arrange */
    givenException();
    givenTraceEnabledIs(false);

    /* Act */
    logException();

    /* Assert */
    verifyExceptionMessageWasWrittenAsError();
  }

  @Test(
    dataProvider = "traceEnabledTestCasesProvider",
    expectedExceptions = NullPointerException.class)
  public void loggingExceptionThrowsOnNullException(final boolean traceEnabledValue) {

    /* Arrange */
    givenNullException();
    givenTraceEnabledIs(traceEnabledValue);

    /* Act */
    logException();
  }

  private void givenException() {
    exception = new NullPointerException("test exception");
  }

  private void givenNullException() {
    exception = null;
  }

  private void givenTraceEnabledIs(final boolean traceIsEnabled) {
    when(wrappedLogger.isTraceEnabled()).thenReturn(traceIsEnabled);
  }

  private void logException() {
    nyaaLogWriter.log(exception);
  }

  private void verifySingleTraceCall() {
    verify(wrappedLogger, times(1)).trace(anyString());
  }

  private void verifyNothingButTrace() {
    verify(wrappedLogger).isTraceEnabled();
    verify(wrappedLogger).trace(anyString());
    verifyNoMoreInteractions(wrappedLogger);
  }

  private void verifyZeroTraceCalls() {
    verify(wrappedLogger, times(0)).trace(anyString());
  }

  private void verifyExceptionStackWasTraced() {
    val exceptionStackTrace = Throwables.getStackTraceAsString(exception);
    verify(wrappedLogger).trace(exceptionStackTrace);
  }

  private void verifySingleErrorCall() {
    verify(wrappedLogger, times(1)).error(anyString());
  }

  private void verifyNothingButError() {
    verify(wrappedLogger).isTraceEnabled();
    verify(wrappedLogger).error(anyString());
    verifyNoMoreInteractions(wrappedLogger);
  }

  private void verifyExceptionMessageWasWrittenAsError() {
    val message = exception.getMessage();
    verify(wrappedLogger).error(message);
  }

  @DataProvider(name = "traceEnabledTestCasesProvider")
  private static Iterator<Object[]> getTraceEnabledTestCases() {
    return TestCases.forBoolean();
  }
}
