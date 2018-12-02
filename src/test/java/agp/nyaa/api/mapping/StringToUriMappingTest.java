package agp.nyaa.api.mapping;

import static org.testng.Assert.assertEquals;

import java.net.URI;

import org.testng.annotations.Test;

public class StringToUriMappingTest {

  private StringToUriMapping mapping = new StringToUriMapping();


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnNull() {
    mapping.apply(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void mappingShouldThrowOnInvalidUriString() {
    mapping.apply("\\some_invalid_link\\");
  }

  @Test
  public void mappingValidUriStringShouldProduceExpectedResult() {

    // given
    final String validUriString = "/download/1032497.torrent";

    // when
    final URI mappingResult = mapping.apply(validUriString);

    // then
    assertEquals(mappingResult.getPath(), validUriString);
  }
}
