package agp.nyaa.api.mirror

import agp.nyaa.api.test.TestDocumentConnection
import agp.nyaa.api.test.TestDocuments
import agp.nyaa.api.test.TestNyaaMirror
import spock.lang.Specification

class NyaaMirrorVerificationSpec extends Specification {

  final NyaaMirrorVerification verification = new NyaaMirrorVerification()


  def "Verification should throw when mirror connection returns no document"() {

    given: "Mirror that never returns a document"
      UnverifiedNyaaMirror unverifiableMirror = newUnverifiableMirror()

    when: "Verification is performed"
      verification.apply(unverifiableMirror)

    then: "Exception should be thrown"
      thrown(NyaaMirrorVerification.Exception)
  }

  def "Verification should throw when mirror connection returns not a TorrentListDocument"() {

    given: "Mirror that never TorrentListDocument"
      UnverifiedNyaaMirror invalidMirror = newMirrorWithoutTorrentListDocument()

    when: "Verification is performed"
      verification.apply(invalidMirror)

    then: "Exception should be thrown"
      thrown(NyaaMirrorVerification.Exception)
  }

  def "Verification should pass when mirror connection returns TorrentListDocument"() {
    expect: verification.apply(newValidNyaaMirror()) != null
  }

  /* utils */

  UnverifiedNyaaMirror newUnverifiableMirror() {
    def connection = new TestDocumentConnection().failing()
    new TestNyaaMirror("https://nyaa.si/", connection).unverified()
  }

  UnverifiedNyaaMirror newMirrorWithoutTorrentListDocument() {
    def nonTorrentListDocument = TestDocuments.get("non-torrent-list-table.html");
    def connection = new TestDocumentConnection().to(nonTorrentListDocument);
    new TestNyaaMirror("https://invalid.mirror/", connection).unverified();
  }

  UnverifiedNyaaMirror newValidNyaaMirror() {
    def torrentListDocument = TestDocuments.get("empty-torrent-list.html");
    def connection = new TestDocumentConnection().to(torrentListDocument);
    new TestNyaaMirror("https://nyaa.si/", connection).unverified();
  }
}