package agp.nyaa.api.mirror;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import agp.nyaa.api.test.TestDocumentConnection;
import agp.nyaa.api.test.TestDocuments;
import agp.nyaa.api.test.TestNyaaMirror;
import lombok.val;

public class NyaaMirrorVerifierTest {

  private NyaaMirrorVerifier verifier = new NyaaMirrorVerifier();


  @Test(expectedExceptions = NyaaMirrorVerifier.Exception.class)
  public void verificationShouldThrowWhenMirrorConnectionReturnsNoDocument() {
    verifier.verify(newUnverifiableMirror());
  }

  @Test(expectedExceptions = NyaaMirrorVerifier.Exception.class)
  public void verificationShouldThrowWhenMirrorConnectionReturnsNonTorrentListDocument() {
    verifier.verify(newMirrorWithoutTorrentListDocument());
  }

  @Test
  public void verificationShouldPassWhenMirrorConnectionReturnsTorrentListDocument() {

    // given
    val validNyaaMirror = newValidNyaaMirror();

    // when
    val verifiedMirror = verifier.verify(validNyaaMirror);

    // then
    assertNotNull(verifiedMirror);
  }

  /* utils */

  private UnverifiedNyaaMirror newUnverifiableMirror() {
    val connection = new TestDocumentConnection().failing();
    return new TestNyaaMirror("https://nyaa.si/", connection).unverified();
  }

  private UnverifiedNyaaMirror newMirrorWithoutTorrentListDocument() {
    val nonTorrentListDocument = TestDocuments.get("non-torrent-list-table.html");
    val connection = new TestDocumentConnection().to(nonTorrentListDocument);
    return new TestNyaaMirror("https://invalid.mirror/", connection).unverified();
  }

  private UnverifiedNyaaMirror newValidNyaaMirror() {
    val torrentListDocument = TestDocuments.get("empty-torrent-list.html");
    val connection = new TestDocumentConnection().to(torrentListDocument);
    return new TestNyaaMirror("https://nyaa.si/", connection).unverified();
  }
}
