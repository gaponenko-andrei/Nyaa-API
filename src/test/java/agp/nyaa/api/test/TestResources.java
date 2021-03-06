package agp.nyaa.api.test;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.testng.TestNGException;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class TestResources {

  private static final Path RESOURCES_DIRECTORY;

  static {
    try {
      RESOURCES_DIRECTORY = Paths.get(
        TestResources.class
          .getClassLoader()
          .getResource("./")
          .toURI()
      );
    } catch (Exception e) {
      throw new TestNGException(e);
    }
  }

  public static Path root() {
    return RESOURCES_DIRECTORY;
  }

  public static Path get(@NonNull final String fileName) {
    return RESOURCES_DIRECTORY.resolve(fileName);
  }
}
