package io.github.nthportal.version

final case class CoreVersion(major: Int, minor: Int, patch: Int) extends Ordered[CoreVersion] {
  if (major < 0 || minor < 0 || patch < 0) {
    throw new IllegalArgumentException("Major, minor, and patch version numbers must be positive")
  }

  override def compare(that: CoreVersion): Int = this compare that

  override def toString: String = major + "." + minor + "." + patch
}

object CoreVersion {
  val v0_0_0 = CoreVersion(0, 0, 0)
  val v0_0_1 = CoreVersion(0, 0, 1)
  val v1_0_0 = CoreVersion(1, 0, 0)

  implicit private val ordering: Ordering[CoreVersion] = Ordering.by(v => (v.major, v.minor, v.patch))
  private val versionLength = 3

  @throws[VersionFormatException]
  def parseVersion(version: String): CoreVersion = {
    // Check that there are exactly 3 numbers in version
    val split = version.split(".")
    if (split.length != versionLength) {
      throw new VersionFormatException(version)
    }

    // Check that all 3 are positive numbers
    try {
      val ints = split map Integer.parseInt
      for (i <- ints if i < 0) throw new VersionFormatException(version)
      CoreVersion(ints(0), ints(1), ints(2))
    } catch {
      case e: NumberFormatException => throw new VersionFormatException(version, e)
    }
  }
}
