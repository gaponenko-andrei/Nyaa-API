package agp.nyaa.api.exception;

import java.net.MalformedURLException;

public class MalformedUrlRuntimeException extends RuntimeException {

  public MalformedUrlRuntimeException(MalformedURLException cause) {
    super(cause);
  }
}
