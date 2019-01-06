package agp.nyaa.api.util

import org.slf4j.Logger
import spock.lang.Specification

import static com.google.common.base.Throwables.getStackTraceAsString

class NyaaLogWriterSpec extends Specification {

  final Exception exception = new NullPointerException("test exception")

  // setup for each test
  Logger wrappedLogger
  NyaaLogWriter nyaaLogWriter

  def setup() {
    wrappedLogger = Mock(Logger)
    nyaaLogWriter = new NyaaLogWriter(wrappedLogger)
  }

  def "Logging should throw on null"() {

    when: "Null is logged"
      nyaaLogWriter.log(null)

    then: "Exception should be thrown"
      thrown(IllegalArgumentException)

    and: "No more interactions should be made"
      0 * _
  }

  def "Logged exception stack should be traced when trace is enabled"() {

    given: "Trace is enabled"
      boolean enabledTrace = true

    when: "Exception is logged"
      new NyaaLogWriter(wrappedLogger).log(exception)

    then: "There should be one check if trace is enabled"
      1 * wrappedLogger.isTraceEnabled() >> enabledTrace

    and: "Exception stack should be traced"
      1 * wrappedLogger.trace(getStackTraceAsString(exception))

    and: "No more interactions should be made"
      0 * _
  }

  def "Logged exception message should be written as error when trace is disabled"() {

    given: "Trace is enabled"
      boolean enabledTrace = false

    when: "Exception is logged"
      new NyaaLogWriter(wrappedLogger).log(exception)

    then: "There should be one check if trace is enabled"
      1 * wrappedLogger.isTraceEnabled() >> enabledTrace

    and: "Exception message should be written as error"
      1 * wrappedLogger.error(exception.message)

    and: "No more interactions should be made"
      0 * _
  }

  def "Method warnAbout() should write exception stack as a warning when trace is enabled"() {

    given: "Trace is enabled"
      boolean enabledTrace = true

    when: "Exception is logged"
      nyaaLogWriter.warnAbout(exception)

    then: "There should be one check if trace is enabled"
      1 * wrappedLogger.isTraceEnabled() >> enabledTrace

    and: "Exception stack trace should be written as warning"
      1 * wrappedLogger.warn(getStackTraceAsString(exception))

    and: "No more interactions should be made"
      0 * _
  }

  def "Method warnAbout() should write exception message as a warning when trace is disabled"() {

    given: "Trace is disabled"
      boolean enabledTrace = false

    when: "Exception is logged"
      nyaaLogWriter.warnAbout(exception)

    then: "There should be one check if trace is enabled"
      1 * wrappedLogger.isTraceEnabled() >> enabledTrace

    and: "Exception message should be written as warning"
      1 * wrappedLogger.warn(exception.message)

    and: "No more interactions should be made"
      0 * _
  }
}