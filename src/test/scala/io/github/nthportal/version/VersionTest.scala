package io.github.nthportal.version

import org.junit.Test
import org.junit.Assert._

import io.github.nthportal.version.{TestReleaseType => TRT}

class VersionTest {
  @Test
  def constructionTest(): Unit = {
    val factory = TRT.basicFactory

    val v1 = factory(BaseVersion.v1_0_0, TRT.Greater)
    assertEquals(BaseVersion.v1_0_0, v1.base)
    assertEquals(TRT.Greater, v1.releaseType)
  }

  @Test
  def compareTest(): Unit = {
    val factory = TRT.basicFactory

    val v1 = factory(BaseVersion.v1_0_0, TRT.Greater)
    val v2 = factory(BaseVersion.v1_0_0, TRT.Lesser)
    val v3 = factory(BaseVersion.v0_0_0, TRT.Greater)

    assertTrue(v1 > v2)
    assertTrue(v1 > v3)
    assertTrue(v2 > v3)
  }

  @Test
  def testToString(): Unit = {
    val factory = TRT.basicFactory

    assertEquals("1.0.0-Greater", factory(BaseVersion.v1_0_0, TRT.Greater).toString)
    assertEquals("1.0.0", factory(BaseVersion.v1_0_0, TRT.Default).toString)
  }
}
