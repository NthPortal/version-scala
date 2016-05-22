package io.github.nthportal.version

/**
  * Represents a [[http://semver.org/ SemVer]] version
  * `[[major]].[[minor]].[[patch]]`.
  *
  * @constructor Constructs a `BaseVersion` using the specified major, minor,
  *              and patch numbers.
  * @param major the major version number
  * @param minor the minor version number
  * @param patch the patch version number
  * @throws IllegalArgumentException if major, minor, or patch values are negative
  */
@throws[IllegalArgumentException]
final case class BaseVersion(major: Int, minor: Int, patch: Int) extends Ordered[BaseVersion] {
  if (major < 0 || minor < 0 || patch < 0) {
    throw new IllegalArgumentException("Major, minor, and patch version numbers must be positive")
  }

  override def compare(that: BaseVersion): Int = BaseVersion.ordering.compare(this, that)

  /**
    * Returns this version formatted as `[[major]].[[minor]].[[patch]]`
    *
    * @return this version represented as a string
    */
  override def toString: String = major + "." + minor + "." + patch
}

object BaseVersion {
  /**
    * A [[BaseVersion]] representing `0.0.0`
    */
  val v0_0_0 = apply(0, 0, 0)

  /**
    * A [[BaseVersion]] representing `0.0.1`
    */
  val v0_0_1 = apply(0, 0, 1)

  /**
    * A [[BaseVersion]] representing `0.1.0`
    */
  val v0_1_0 = apply(0, 1, 0)

  /**
    * A [[BaseVersion]] representing `1.0.0`
    */
  val v1_0_0 = apply(1, 0, 0)

  private val ordering: Ordering[BaseVersion] = Ordering.by(v => (v.major, v.minor, v.patch))

  /**
    * Parses a version string (`x.x.x`) into a [[BaseVersion]].
    *
    * @param version a string in the format `x.x.x` to parse into a [[BaseVersion]]
    * @throws VersionFormatException if `version` is not of the correct format
    * @return a BaseVersion represented by the version string
    */
  @throws[VersionFormatException]
  def parseVersion(version: String): BaseVersion = {
    version.split('.') match {
      case Array(major, minor, patch) =>
        try {
          apply(Integer.parseInt(major), Integer.parseInt(minor), Integer.parseInt(patch))
        } catch {
          case e@(_: IllegalArgumentException | _: NumberFormatException) =>
            throw new VersionFormatException(version, e)
        }
      case _ => throw new VersionFormatException(version)
    }
  }
}
