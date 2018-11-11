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
    exception = new NullPointerException("test exception");
    wrappedLogger = mock(Logger.class);
    nyaaLogWriter = new NyaaLogWriter(wrappedLogger);
  }

  @Test
  public void loggingExceptionWhenTraceEnabledShouldMakeSingleTraceCall() {

    // Given
    givenTraceEnabled();

    // When
    log(exception);

    // Then
    verifySingleTraceCall();
  }

  @Test
  public void loggingExceptionWhenTraceEnabledShouldMakeNothingButTraceCall() {

    // Given
    givenTraceEnabled();

    // When
    log(exception);

    // Then
    verifyNothingButTraceCall();
  }

  @Test
  public void loggedExceptionStackShouldBeTracedWhenTraceEnabled() {

    // Given
    givenTraceEnabled();

    // When
    log(exception);

    // Then
    verifyExceptionStackWasTraced();
  }

  @Test
  public void loggingExceptionWhenTraceDisabledShouldMakeZeroTraceCalls() {

    // Given
    givenTraceDisabled();

    // When
    log(exception);

    // Then
    verifyZeroTraceCalls();
  }

  @Test
  public void loggingExceptionWhenTraceDisabledShouldMakeErrorCallInstead() {

    // Given
    givenTraceDisabled();

    // When
    log(exception);

    // Then
    verifySingleErrorCall();
  }

  @Test
  public void loggingExceptionWhenTraceDisabledShouldMakeNothingButErrorCall() {

    // Given
    givenTraceDisabled();

    // When
    log(exception);

    // Then
    verifyNothingButErrorCall();
  }

  @Test
  public void loggedExceptionMessageShouldBeWrittenAsErrorWhenTraceDisabled() {

    // Given
    givenTraceDisabled();

    // When
    log(exception);

    // Then
    verifyExceptionMessageWasWrittenAsError();
  }

  @Test(dataProvider = "traceEnabledTestCasesProvider",
    expectedExceptions = IllegalArgumentException.class)
  public void loggingShouldThrowOnNulls(final boolean traceEnabledValue) {

    // Given
    givenTraceEnabledIs(traceEnabledValue);

    // When
    log(null);
  }

  private void givenTraceEnabled() {
    givenTraceEnabledIs(true);
  }

  private void givenTraceDisabled() {
    givenTraceEnabledIs(false);
  }

  private void givenTraceEnabledIs(final boolean traceIsEnabled) {
    when(wrappedLogger.isTraceEnabled()).thenReturn(traceIsEnabled);
  }

  private void log(Exception exception) {
    nyaaLogWriter.log(exception);
  }

  private void verifySingleTraceCall() {
    verify(wrappedLogger, times(1)).trace(anyString());
  }

  private void verifyNothingButTraceCall() {
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

  private void verifyNothingButErrorCall() {
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
