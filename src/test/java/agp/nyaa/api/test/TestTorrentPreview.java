package agp.nyaa.api.test;

import java.net.URI;
import java.time.Instant;

import com.google.common.primitives.UnsignedInteger;

import agp.nyaa.api.mapper.StringToUriMapper;
import agp.nyaa.api.model.Category;
import agp.nyaa.api.model.DataSize;
import agp.nyaa.api.model.TorrentPreview;
import agp.nyaa.api.model.TorrentState;

public final class TestTorrentPreview extends TorrentPreview {

  @Override
  public Long id() {
    return 1L;
  }

  @Override
  public TorrentState state() {
    return TorrentState.NORMAL;
  }

  @Override
  public Category category() {
    return Category.Anime.NonEnglishTranslated.instance();
  }

  @Override
  public String title() {
    return "title";
  }

  @Override
  public URI downloadLink() {
    return StringToUriMapper.applicationTo("http://nyaa.si/test/download-link/");
  }

  @Override
  public URI magnetLink() {
    return StringToUriMapper.applicationTo("http://nyaa.si/test/magnet-link/");
  }

  @Override
  public DataSize dataSize() {
    return DataSize.of(10, DataSize.Unit.BYTE);
  }

  @Override
  public Instant uploadInstant() {
    return Instant.now();
  }

  @Override
  public UnsignedInteger seedersCount() {
    return UnsignedInteger.valueOf(10);
  }

  @Override
  public UnsignedInteger leechersCount() {
    return UnsignedInteger.valueOf(20);
  }

  @Override
  public UnsignedInteger downloadsCount() {
    return UnsignedInteger.valueOf(30);
  }
}
