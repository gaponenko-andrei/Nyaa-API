package agp.nyaa.api.parsing

import agp.nyaa.api.element.Table
import agp.nyaa.api.model.TorrentPreview
import agp.nyaa.api.test.TestDocuments
import com.google.common.collect.ImmutableSet
import org.jsoup.nodes.Element
import spock.lang.Specification

class TorrentListTableParsingSpec extends Specification {

  def "Using null torrent preview parsing should throw"() {

    when:
      TorrentListTableParsing.using((TorrentPreviewTrParsing) null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Parsing should throw on null"() {

    given:
      def parsing = TorrentListTableParsing.using(new TorrentPreviewTrParsing())

    when:
      parsing.apply(null)

    then:
      thrown(IllegalArgumentException)
  }

  def "Parsing should default torrent preview parsing should produce expected results"() {

    given:
      def parsing = TorrentListTableParsing.using(new TorrentPreviewTrParsing())

    when:
      ImmutableSet<TorrentPreview> result = parsing.apply(newNonEmptyTorrentListTable())

    then:
      result*.id() == [1060623L, 1060486L, 1060485L, 1060483L]
  }

  /* utils */

  Table newNonEmptyTorrentListTable() {
    selectFirstTableFrom("torrents-list-parsing-test.html");
  }

  Table newNonTorrentListTable() {
    selectFirstTableFrom("non-torrent-list-table.html");
  }

  Table selectFirstTableFrom(final String testDocumentName) {
    def asTable = { Element element -> new Table(element) }
    TestDocuments.get(testDocumentName).select("table", asTable).get(0);
  }

}