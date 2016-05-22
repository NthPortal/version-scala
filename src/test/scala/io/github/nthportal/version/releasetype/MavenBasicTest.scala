package io.github.nthportal.version.releasetype

import io.github.nthportal.version.BaseVersion
import org.junit.Test
import org.junit.Assert._
import io.github.nthportal.version.releasetype.{MavenBasic => Mvn}

class MavenBasicTest {
  @Test
  def compareTest(): Unit = {
    assertTrue(Mvn.Release > Mvn.Snapshot)
  }

  @Test
  def extensionTest(): Unit = {
    assertEquals("SNAPSHOT", Mvn.Snapshot.extension)
  }

  @Test
  def testToString(): Unit = {
    assertEquals("SNAPSHOT", Mvn.Snapshot.toString)
  }

  @Test
  def defaultFactoryTest(): Unit = {
    val factory = Mvn.defaultFactory

    // Construction and toString tests
    val v1 = factory(BaseVersion.v1_0_0, Mvn.Snapshot)
    assertEquals(Mvn.Snapshot, v1.releaseType)
    assertEquals("1.0.0-SNAPSHOT", v1.toString)

    val v2 = factory(BaseVersion.v0_1_0, Mvn.Release)
    assertEquals(Mvn.Release, v2.releaseType)
    assertEquals("0.1.0", v2.toString)

    // Parse tests
    assertEquals(v1, factory.parseVersion("1.0.0-SNAPSHOT"))
    assertEquals(v2, factory.parseVersion("0.1.0"))

    (factory.tryParseVersion("1.0.0-RELEASE") ::
      factory.tryParseVersion("1.0") ::
      Nil)
      .foreach(t => assertTrue(t.isFailure))
  }
}