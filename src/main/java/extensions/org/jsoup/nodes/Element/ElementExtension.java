package extensions.org.jsoup.nodes.Element;

import lombok.NonNull;
import manifold.ext.api.Extension;
import manifold.ext.api.This;
import org.jsoup.nodes.Element;
import properties.Constants;

@Extension
public class ElementExtension {

  public static boolean isTorrentList(@NonNull @This Element thiz) {
    return thiz.hasClass(Constants.torrent.list.className);
  }
}
