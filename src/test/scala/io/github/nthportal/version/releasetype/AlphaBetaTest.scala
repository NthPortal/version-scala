package io.github.nthportal.version.releasetype

import org.scalatest.FunSuite

class AlphaBetaTest extends FunSuite {
  test("toString for ReleaseCandidate works properly") {
    val rc1 = AlphaBeta.ReleaseCandidate(1)
    assert(rc1.toString == rc1.extension)
    assert(rc1.extension == "rc.1")
  }
}
