package io.github.nthportal.version

import org.scalatest.FunSuite

class BaseVersionTest extends FunSuite {
  test("constructor works properly") {
    // Check basic construction and fields
    val validVersion = BaseVersion(1, 2, 3)
    assert(validVersion.major == 1)
    assert(validVersion.minor == 2)
    assert(validVersion.patch == 3)

    // Check that constructor does not accept negative values
    for { major <- -1 to 0
          minor <- -1 to 0
          patch <- -1 to 0
          if major < 0 || minor < 0 || patch < 0
    } intercept[IllegalArgumentException] { BaseVersion(major, minor, patch) }
  }
  
  test("comparison works properly") {
    import BaseVersion._

    // Test all permutations of having a single different field and being greater
    assert(v1_0_0 > v0_0_0)
    assert(v0_1_0 > v0_0_0)
    assert(v0_0_1 > v0_0_0)

    // Test all permutations of having two different fields and being greater
    assert(v1_0_0 > v0_1_0)
    assert(v1_0_0 > v0_0_1)
    assert(v0_1_0 > v0_0_1)


    // Test permutations of having 3 different fields and being greater
    val v1_1_1 = BaseVersion(1, 1, 1)
    val v1_1_0 = BaseVersion(1, 1, 0)
    val v1_0_1 = BaseVersion(1, 0, 1)
    val v0_1_1 = BaseVersion(0, 1, 1)

    assert(v1_1_1 > v0_0_0)
    assert(v1_1_0 > v0_0_1)
    assert(v1_0_1 > v0_1_0)
    assert(v1_0_0 > v0_1_1)

    // Test that multi-digit fields compare correctly
    val v_10_0_0 = BaseVersion(10, 0, 0)
    assert(v_10_0_0 > v1_0_0)
  }

  test("toString works correctly") {
    assert(BaseVersion.v1_0_0.toString == "1.0.0")
    assert(BaseVersion(10, 2, 20).toString == "10.2.20")
  }

  test("version parsing works correctly") {
    // Check that valid strings yield the correct BaseVersion
    assert(BaseVersion.parseVersion("1.0.0") == BaseVersion.v1_0_0)
    assert(BaseVersion.parseVersion("10.0.3") == BaseVersion(10, 0, 3))

    // Check that strings with the wrong number of '.'-separated numbers fail to parse
    ("" :: "3" :: "1.0" :: "1.0.0.2" :: Nil)
      .foreach(s => intercept[VersionFormatException] { BaseVersion.parseVersion(s) })

    // Check improperly formatted numbers
    ("-1.3.3" :: "0.-2.3" :: "2.4.-9" :: "-3.-2.-6" :: Nil)
      .foreach(s => intercept[VersionFormatException] { BaseVersion.parseVersion(s) })
  }
}
