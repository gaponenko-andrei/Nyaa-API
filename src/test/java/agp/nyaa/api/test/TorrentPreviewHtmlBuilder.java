package agp.nyaa.api.test;

import agp.nyaa.api.model.Category;
import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.model.TorrentState;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import static j2html.TagCreator.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Instance", builderMethodName = "instance")
public class TorrentPreviewHtmlBuilder implements Supplier<ContainerTag> {

  private TorrentState state;
  private Integer torrentId;
  private Category category;
  private String title;
  private Integer commentCount;
  private URL torrentDownloadLink;
  private URI magnetDownloadLink;
  private DataSize dataSize;
  private LocalDateTime creationDate;
  private Integer seedersCount;
  private Integer leechersCount;
  private Integer completedDownloadsCount;

  public ContainerTag build() {
    final ContainerTag containerTag = tr();

//    if (state != null)
//      setStateTo(containerTag);


    return containerTag;
  }

//  private void setStateTo(final ContainerTag containerTag) {
//    final String stateClassName = new TorrentStateMapper().reverse().mapper(state);
//    containerTag.withClass(stateClassName);
//  }

//  private void appendCategoryTo(final ContainerTag containerTag) {
//    final DomContent categoryLink =
//
//    containerTag.with(
//            td(
//                    a().withHref("category_link").withTitle(category.name())
//            )
//    );
//  }


  @Override
  public ContainerTag get() {
    return build();
  }
}
