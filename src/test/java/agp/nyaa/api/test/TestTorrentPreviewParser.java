package agp.nyaa.api.test;

import agp.nyaa.api.element.ImmutableElement;
import agp.nyaa.api.element.Tr;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.parsing.Parser;
import org.jsoup.nodes.Element;

public class TestTorrentPreviewParser implements Parser<Tr, TorrentPreview> {

  @Override
  public TorrentPreview apply(final Tr element) {
    return new TestTorrentPreview();
  }
}
