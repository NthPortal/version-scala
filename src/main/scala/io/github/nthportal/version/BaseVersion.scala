package io.github.nthportal.version

final case class BaseVersion(major: Int, minor: Int, patch: Int) extends Ordered[BaseVersion] {
  if (major < 0 || minor < 0 || patch < 0) {
    throw new IllegalArgumentException("Major, minor, and patch version numbers must be positive")
  }

  override def compare(that: BaseVersion): Int = this compare that

  override def toString: String = major + "." + minor + "." + patch
}

object BaseVersion {
  val v0_0_0 = BaseVersion(0, 0, 0)
  val v0_0_1 = BaseVersion(0, 0, 1)
  val v1_0_0 = BaseVersion(1, 0, 0)

  implicit private val ordering: Ordering[BaseVersion] = Ordering.by(v => (v.major, v.minor, v.patch))

  @throws[VersionFormatException]
  def parseVersion(version: String): BaseVersion = {
    version.split(".") match {
      case Array(major, minor, patch) =>
        try {
          BaseVersion(Integer.parseInt(major), Integer.parseInt(minor), Integer.parseInt(patch))
        } catch {
          case e @ (_: IllegalArgumentException | _: NumberFormatException) =>
            throw new VersionFormatException(version, e)
        }
      case _ => throw new VersionFormatException(version)
    }
  }
}
