package agp.nyaa.api.mapper;

import agp.nyaa.api.exception.parse.ParseException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;

import static org.testng.Assert.assertNotNull;

public class StringToUriMapperTest {

  private StringToUriMapper mapper;
  private String uriString;
  private URI resultingUri;

  @BeforeMethod
  public void setUp() {
    mapper = new StringToUriMapper();
  }

  @Test
  public void mapping() {

    /* Arrange */
    givenValidUriString();

    /* Act */
    mapUriString();

    /* Assert */
    assertNotNull(resultingUri);
  }

  @Test(expectedExceptions = ParseException.class)
  public void mapperThrowsOnInvalidUriString() {

    /* Arrange */
    givenInvalidUriString();

    /* Act */
    mapUriString();
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void mapperThrowsOnNullUriString() {

    /* Arrange */
    givenNullUriString();

    /* Act */
    mapUriString();
  }

  //
  // 'Arrange' methods
  //

  private void givenValidUriString() {
    uriString = "/download/1032497.torrent";
  }

  private void givenInvalidUriString() {
    uriString = "\\some_invalid_link\\";
  }

  private void givenNullUriString() {
    uriString  = null;
  }

  //
  // 'Act' methods
  //

  private void mapUriString() {
    resultingUri = mapper.map(uriString);
  }
}
