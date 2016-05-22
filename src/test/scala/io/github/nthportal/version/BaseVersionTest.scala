package io.github.nthportal.version

import org.junit.Assert._
import org.junit.Test

import scala.util.Try

/**
  * Tests for [[BaseVersion]]
  */
class BaseVersionTest {
  /**
    * Test that values are assigned and validated properly during construction.
    */
  @Test
  def constructionTest(): Unit = {
    // Check basic construction and fields
    val validVersion = BaseVersion(1, 2, 3)
    assertEquals(1, validVersion.major)
    assertEquals(2, validVersion.minor)
    assertEquals(3, validVersion.patch)

    // Check that constructor does not accept negative values
    val tries = for {
      major <- -1 to 0
      minor <- -1 to 0
      patch <- -1 to 0
      if major < 0 || minor < 0 || patch < 0
    } yield Try(BaseVersion(major, minor, patch))

    assertEquals(7, tries.length) // Check that the for comprehension is correct
    tries.foreach(t => {
      assertTrue(t.isFailure)
      assertTrue(t.failed.get.isInstanceOf[IllegalArgumentException])
    })
  }

  /**
    * Test that comparison works correctly.
    */
  @Test
  def compareTest(): Unit = {
    import BaseVersion._

    // Test all permutations of having a single different field and being greater
    assertTrue(v1_0_0 > v0_0_0)
    assertTrue(v0_1_0 > v0_0_0)
    assertTrue(v0_0_1 > v0_0_0)

    // Test all permutations of having two different fields and being greater
    assertTrue(v1_0_0 > v0_1_0)
    assertTrue(v1_0_0 > v0_0_1)
    assertTrue(v0_1_0 > v0_0_1)


    // Test permutations of having 3 different fields and being greater
    val v1_1_1 = BaseVersion(1, 1, 1)
    val v1_1_0 = BaseVersion(1, 1, 0)
    val v1_0_1 = BaseVersion(1, 0, 1)
    val v0_1_1 = BaseVersion(0, 1, 1)

    assertTrue(v1_1_1 > v0_0_0)
    assertTrue(v1_1_0 > v0_0_1)
    assertTrue(v1_0_1 > v0_1_0)
    assertTrue(v1_0_0 > v0_1_1)

    // Test that multi-digit fields compare correctly
    val v_10_0_0 = BaseVersion(10, 0, 0)
    assertTrue(v_10_0_0 > v1_0_0)
  }

  /**
    * Test that [[BaseVersion.toString]] works correctly.
    */
  @Test
  def testToString(): Unit = {
    import BaseVersion._

    assertEquals("1.0.0", v1_0_0.toString)
    assertEquals("10.2.20", BaseVersion(10, 2, 20).toString)
  }

  /**
    * Test parsing [[BaseVersion]]s from string.
    */
  @Test
  def parseVersionTest(): Unit = {
    // Check that valid strings yield the correct BaseVersion
    assertEquals(BaseVersion.v1_0_0, BaseVersion.parseVersion("1.0.0"))
    assertEquals(BaseVersion(10, 0, 3), BaseVersion.parseVersion("10.0.3"))

    // Check that strings with the wrong number of '.'-separated numbers fail to parse
    (BaseVersion.tryParseVersion("") ::
      BaseVersion.tryParseVersion("3") ::
      BaseVersion.tryParseVersion("1.0") ::
      BaseVersion.tryParseVersion("1.0.0.2") ::
      Nil)
      .foreach(t => {
        assertTrue(t.isFailure)
        assertTrue(t.failed.get.isInstanceOf[VersionFormatException])
      })

    // Check improperly formatted numbers
    (BaseVersion.tryParseVersion("-1.3.3") ::
      BaseVersion.tryParseVersion("0.-2.3") ::
      BaseVersion.tryParseVersion("2.4.-9") ::
      BaseVersion.tryParseVersion("-3.-2.-6") ::
      Nil)
      .foreach(t => {
        assertTrue(t.isFailure)
        assertTrue(t.failed.get.isInstanceOf[VersionFormatException])
      })
  }
}
