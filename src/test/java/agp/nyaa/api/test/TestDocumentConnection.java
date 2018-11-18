package agp.nyaa.api.test;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;

import agp.nyaa.api.element.ImmutableDocument;
import agp.nyaa.api.util.DocumentConnection;
import agp.nyaa.api.util.Try;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PRIVATE)
public final class TestDocumentConnection extends DocumentConnection {

  ImmutableDocument document;

  @Override
  public URL url() {
    return Try.call(() -> new URL("https://nyaa.si")).result();
  }

  @Override
  public Try<GetRs> getRs() {
    GetRs mockRs = mock(GetRs.class);

    if (document != null) {
      when(mockRs.document()).thenReturn(document);
      return Try.success(mockRs);
    }

    return Try.failure(new RuntimeException("Test failure reason."));
  }

  public TestDocumentConnection to(@NonNull final ImmutableDocument document) {
    this.document = document;
    return this;
  }

  public TestDocumentConnection failing() {
    this.document = null;
    return this;
  }
}
