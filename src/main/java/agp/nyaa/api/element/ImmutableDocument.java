package agp.nyaa.api.element;

import org.jsoup.nodes.Document;

@javax.annotation.concurrent.Immutable
public final class ImmutableDocument extends ImmutableElement<Document> {

  public ImmutableDocument(final Document sourceDocument) {
    super(sourceDocument);
  }
}
