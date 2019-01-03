package agp.nyaa.api.parsing

import agp.nyaa.api.element.ImmutableDocument
import agp.nyaa.api.element.Tr
import agp.nyaa.api.mapping.StringToUriMapping
import agp.nyaa.api.model.Category
import agp.nyaa.api.model.TorrentPreview
import agp.nyaa.api.model.TorrentState
import agp.nyaa.api.test.TestDocuments
import com.google.common.primitives.UnsignedInteger
import org.jsoup.nodes.Element
import spock.lang.Specification

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TorrentPreviewTrParsingSpec extends Specification {

  final TorrentPreviewTrParsing parsing = new TorrentPreviewTrParsing()

  // setup source of test elements once, to avoid unnecessary file-reads
  final ImmutableDocument trSamplesDocument = TestDocuments.get("torrent-preview-tr-samples.html")

  // setup valid source case once, as it is immutable
  final Tr validTorrentPreviewTr = newTorrentPreviewTr("valid")


  def "Parsing should throw on null"() {

    when:
      parsing.apply(null)

    then:
      thrown(IllegalArgumentException)
  }

  /* id */

  def "Parsing should produce expected id"() {

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.id() == 1032497L
  }

  def "Parsing should throw on tr without id"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-id"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with invalid id"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-invalid-id"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* state */

  def "Parsing should produce expected state"() {

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.state() == TorrentState.NORMAL
  }

  def "Parsing should throw on tr without state"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-state"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with unknown state"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-unknown-state"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* category */

  def "Parsing should produce expected category"() {

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.category() == Category.Anime.EnglishTranslated.instance()
  }

  def "Parsing should throw on tr without category"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-category"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with unknown category"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-unknown-category"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* title */

  def "Parsing should produce expected title"() {

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.title() == "[Erai-raws] Tokyo Ghoul-re - 05 [720p].mkv"
  }

  def "Parsing should throw on tr without title"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-title"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* torrent download uri */

  def "Parsing should produce expected torrent download link"() {

    given:
      URI expectedDownloadLink = StringToUriMapping.applicationTo("/download/1032497.torrent")

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.downloadLink() == expectedDownloadLink
  }

  def "Parsing should throw on tr without torrent download link"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-torrent-download-uri"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with invalid torrent download link"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-invalid-torrent-download-uri"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* magnet uri */

  def "Parsing should produce expected magnet link"() {

    given:
      URI expectedMagnetLink = StringToUriMapping.applicationTo(
        "magnet:?xt=urn:btih:BGS7JJ4XMFBB36BRTQVDJ75E5BMUJISR&dn=%5BErai-raws%5D+Tokyo+Ghoul-re+-+05" +
          "+%5B720p%5D.mkv&tr=http%3A%2F%2Fnyaa.tracker.wf%3A7777%2Fannounce&tr=udp%3A%2F%2Fopen." +
          "stealth.si%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.opentrackr.org%3A1337%2Fannounce&tr=" +
          "udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.leechers-" +
          "paradise.org%3A6969%2Fannounce")

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.magnetLink() == expectedMagnetLink
  }

  def "Parsing should throw on tr without magnet link"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-magnet-uri"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with invalid magnet link"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-invalid-magnet-uri"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* upload instant */

  def "Parsing should produce expected upload instant"() {

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.uploadInstant() == newExpectedInstantOf("2018-05-01 16:10")
  }

  def "Parsing should throw on tr without upload instant"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-upload-instant"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with invalid upload instant"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-invalid-upload-instant"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* seeders count */

  def "Parsing should produce expected seeders count"() {

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.seedersCount() == UnsignedInteger.valueOf(1)
  }

  def "Parsing should throw on tr without seeders count"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-seeders-count"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with invalid seeders count"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-invalid-seeders-count"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* leechers count */

  def "Parsing should produce expected leechers count"() {

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.leechersCount() == UnsignedInteger.valueOf(39)
  }

  def "Parsing should throw on tr without leechers count"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-leechers-count"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with invalid leechers count"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-invalid-leechers-count"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* leechers count */

  def "Parsing should produce expected downloads count"() {

    when:
      TorrentPreview result = parsing.apply(validTorrentPreviewTr)

    then:
      result.downloadsCount() == UnsignedInteger.valueOf(0)
  }

  def "Parsing should throw on tr without downloads count"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-absent-downloads-count"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  def "Parsing should throw on tr with invalid downloads count"() {

    when:
      parsing.apply(newTorrentPreviewTr("with-invalid-downloads-count"))

    then:
      thrown(TorrentPreviewTrParsing.Exception)
  }

  /* utils */

  Instant newExpectedInstantOf(String instantString) {
    def expectedDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    def expectedLocalDateTime = LocalDateTime.parse(instantString, expectedDateFormatter)
    expectedLocalDateTime.toInstant(ZoneOffset.UTC)
  }

  Tr newTorrentPreviewTr(final String sampleId) {
    def selector = String.format("tr[data-sample-id='%s']", sampleId)
    def asTr = { Element element -> new Tr(element) }
    trSamplesDocument.select(selector, asTr).get(0)
  }
}