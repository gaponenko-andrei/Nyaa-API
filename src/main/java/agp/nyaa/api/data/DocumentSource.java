package agp.nyaa.api.data;

import org.jsoup.nodes.Document;

import java.nio.file.Path;

public interface DocumentSource {
  Document getDocumentBy(Path path);
}
