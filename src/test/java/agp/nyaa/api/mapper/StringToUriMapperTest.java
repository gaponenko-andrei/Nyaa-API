package agp.nyaa.api.mapper;

import agp.nyaa.api.exception.parse.ParseException;
import org.testng.annotations.Test;

import java.net.URI;

import static org.testng.Assert.assertEquals;

public class StringToUriMapperTest {

  private StringToUriMapper mapper = new StringToUriMapper();

  @Test
  public void validUriStringMapping() {

    /* Arrange */
    final String validUriString = "/download/1032497.torrent";

    /* Act */
    final URI mappingResult = mapper.map(validUriString);

    /* Assert */
    assertEquals(mappingResult.getPath(), validUriString);
  }

  @Test(expectedExceptions = ParseException.class)
  public void mapperThrowsOnInvalidUriString() {
    mapper.map("\\some_invalid_link\\");
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void mapperThrowsOnNullUriString() {
    mapper.map(null);
  }
}
