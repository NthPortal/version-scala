package io.github.nthportal.version.releasetype

import org.junit.Assert._
import org.junit.Test

class AlphaBetaTest {
  @Test
  @throws[Exception]
  def testRCToString() {
    val rc1 = AlphaBeta.ReleaseCandidate(1)
    assertEquals(rc1.extension, rc1.toString)
  }
}
