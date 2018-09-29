package agp.nyaa.api.mapper;

import agp.nyaa.api.exception.parse.ParseException;
import lombok.NonNull;
import lombok.val;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;

public final class StringToUriMapper implements Mapper<String, URI> {

  public static URI applicationTo(@NonNull final String uriString) {
    return new StringToUriMapper().apply(uriString);
  }

  @Override
  public URI apply(@NonNull final String uriString) {
    try {
      return new URI(uriString);
    } catch (URISyntaxException exception) {
      val message = format("Failed to parse provided string value of '%s' as URI.", uriString);
      throw new ParseException(message, exception);
    }
  }
}
