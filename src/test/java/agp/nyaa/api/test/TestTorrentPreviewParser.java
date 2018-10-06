package agp.nyaa.api.test;

import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.parsing.Parser;
import org.jsoup.nodes.Element;

public class TestTorrentPreviewParser implements Parser<Element, TorrentPreview> {

  @Override
  public TorrentPreview apply(Element element) {
    return new TestTorrentPreview();
  }
}
