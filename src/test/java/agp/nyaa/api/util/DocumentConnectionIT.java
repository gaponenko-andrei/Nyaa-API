package agp.nyaa.api.util;

import static agp.nyaa.api.util.DocumentConnection.*;
import static agp.nyaa.api.util.Functions.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import agp.nyaa.api.test.TestResources;
import lombok.val;

public class DocumentConnectionIT {

  @Test
  public void call() throws IOException, URISyntaxException {
    val mirrorsConfig = TestResources.get("mirrors.config");
    val lines = Files.lines(mirrorsConfig).collect(toList());

    List<URL> mirrorUris = new ArrayList<>();
    for (String uriString : lines) {
      mirrorUris.add(new URL(uriString));
    }

    mirrorUris.parallelStream()
      .map(DocumentConnection::fromUrl)
      .map(to(DocumentConnection::getRs))
      .filter(Try::isSuccess)
      .map(to(Try::result))
      .min(comparing(GetRs::timeTaken))
      .ifPresent(System.out::println);
  }
}
