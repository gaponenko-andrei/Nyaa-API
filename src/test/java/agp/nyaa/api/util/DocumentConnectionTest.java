package agp.nyaa.api.util;

import agp.nyaa.api.test.TestResources;
import lombok.val;
import org.jsoup.nodes.Document;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Predicate;

import static agp.nyaa.api.util.DocumentConnection.GetRs.bySuccessFirst;
import static agp.nyaa.api.util.DocumentConnection.GetRs.byTimeTaken;
import static java.util.stream.Collectors.toList;

public class DocumentConnectionTest {

  @Test
  public void call() throws IOException {
    val mirrorsConfig = TestResources.get("mirrors.config");
    val lines = Files.lines(mirrorsConfig).collect(toList());
    val rsRequiredOrdering = bySuccessFirst().thenComparing(byTimeTaken());

    val documentGetRsList = lines.parallelStream()
      .map(this::urlStringToDocumentConnection)
      .map(DocumentConnection::toNewGetRs)
      .sorted(rsRequiredOrdering)
      .collect(toList());

    documentGetRsList.forEach(System.out::println);
  }

  private DocumentConnection urlStringToDocumentConnection(final String documentUri) {
    return new DocumentConnection.Builder().url(documentUri).filter(torrentListDocument()).build();
  }

  private Predicate<Document> torrentListDocument() {
    return document -> document.select("table.torrent-list").size() == 1;
  }
}
