package io.github.nthportal.version.releasetype

import io.github.nthportal.version.{BaseVersion, VersionFormatException}
import io.github.nthportal.version.releasetype.{MavenBasic => Mvn}
import org.scalatest.FunSuite

class MavenBasicTest extends FunSuite {
  test("comparison works properly") {
    assert(Mvn.Release > Mvn.Snapshot)
  }
  
  test("extension and toString are correct") {
    val expected = "SNAPSHOT"
    assert(Mvn.Snapshot.extension == expected)
    assert(Mvn.Snapshot.toString == expected)
  }
  
  test("default factory works correctly") {
    val factory = Mvn.defaultFactory

    // Construction and toString tests
    val v1 = factory(BaseVersion.v1_0_0, Mvn.Snapshot)
    assert(v1.releaseType == Mvn.Snapshot)
    val v1Expected = "1.0.0-SNAPSHOT"
    assert(v1.toString == v1Expected)

    val v2 = factory(BaseVersion.v0_1_0, Mvn.Release)
    assert(v2.releaseType == Mvn.Release)
    val v2Expected = "0.1.0"
    assert(v2.toString == v2Expected)

    // Parse tests
    assert(factory.parseVersion(v1Expected) == v1)
    assert(factory.parseVersion(v2Expected) == v2)

    ("1.0.0-RELEASE" :: "1.0" :: Nil)
      .foreach(s => intercept[VersionFormatException] { factory.parseVersion(s) })
  }
}