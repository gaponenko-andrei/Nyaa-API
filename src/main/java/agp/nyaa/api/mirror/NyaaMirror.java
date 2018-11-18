package agp.nyaa.api.mirror;

import java.net.URL;

import agp.nyaa.api.util.DocumentConnection;

public interface NyaaMirror {
  
  URL url();

  default DocumentConnection connection() {
    return DocumentConnection.to(url());
  }
}
