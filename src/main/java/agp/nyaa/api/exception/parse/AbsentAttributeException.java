package agp.nyaa.api.exception.parse;

public class AbsentAttributeException extends ParseException {

  public AbsentAttributeException(final String message) {
    super(message);
  }

  public AbsentAttributeException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
