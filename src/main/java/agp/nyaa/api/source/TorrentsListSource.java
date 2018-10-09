package agp.nyaa.api.source;

import lombok.NonNull;
import lombok.val;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;


public final class TorrentsListSource extends ElementSourceFilter<Document, Element> {

  public static TorrentsListSource filtering(@NonNull final ElementSource<Document> elementSource) {
    return new TorrentsListSource(elementSource);
  }

  private TorrentsListSource(@NonNull final ElementSource<Document> elementSource) {
    super(elementSource);
  }

  @Override
  Element filter(@NonNull final Document listDocument) {
    val listTables = listDocument.select("table.torrent-list");

    checkArgument(
      listTables.size() == 1,
      "Provided 'listDocument' must have exactly one 'table.torrent-list'."
    );

    return listTables.first();
  }
}
