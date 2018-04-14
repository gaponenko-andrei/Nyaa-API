package agp.nyaa.api.parser;

import agp.nyaa.api.model.TorrentPreview;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;

public final class TorrentListElementParser implements Parser<ImmutableList<TorrentPreview>> {

    @Override
    public ImmutableList<TorrentPreview> parse(@NonNull final Element element) {
        checkArgument(element.isTorrentList());
        return null;
    }
}
