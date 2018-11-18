package agp.nyaa.api.mapper;

import agp.nyaa.api.exception.parse.ParseException;
import org.testng.annotations.Test;

import java.net.URI;

import static org.testng.Assert.assertEquals;

public class StringToUriMapperTest {

  private StringToUriMapper mapper = new StringToUriMapper();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnNulls() {
    mapper.map(null);
  }

  @Test(expectedExceptions = ParseException.class)
  public void mappingShouldThrowOnInvalidUriStrings() {
    mapper.map("\\some_invalid_link\\");
  }

  @Test
  public void mappingValidUriStringShouldProduceExpectedResult() {

    // given
    final String validUriString = "/download/1032497.torrent";

    // when
    final URI mappingResult = mapper.map(validUriString);

    // then
    assertEquals(mappingResult.getPath(), validUriString);
  }
}
