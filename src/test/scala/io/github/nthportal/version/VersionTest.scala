package io.github.nthportal.version

import org.scalatest.FunSuite
import io.github.nthportal.version.{TestReleaseType => TRT}


class VersionTest extends FunSuite {
  test("construction works properly") {
    val factory = TRT.basicFactory

    val v1 = factory(BaseVersion.v1_0_0, TRT.Greater)
    assert(v1.base == BaseVersion.v1_0_0)
    assert(v1.releaseType == TRT.Greater)
  }

  test("comparison works properly") {
    val factory = TRT.basicFactory

    val v1 = factory(BaseVersion.v1_0_0, TRT.Greater)
    val v2 = factory(BaseVersion.v1_0_0, TRT.Lesser)
    val v3 = factory(BaseVersion.v0_0_0, TRT.Greater)

    assert(v1 > v2)
    assert(v1 > v3)
    assert(v2 > v3)
  }

  test("toString works properly") {
    val factory = TRT.basicFactory

    assert(factory(BaseVersion.v1_0_0, TRT.Greater).toString == "1.0.0-Greater")
    assert(factory(BaseVersion.v1_0_0, TRT.Default).toString == "1.0.0")
  }
}
