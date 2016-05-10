package io.github.nthportal.version

/**
  * Represents the release type of a [[Version]]; for example `SNAPSHOT`, `beta`, etc.
  */
trait ReleaseType {
  /**
    * A string which is how this release type should represented at the end
    * a version (`release_type` in `x.x.x-release_type`).
    *
    * @return the string representation of this release type when an extension
    *         to a version
    */
  def extension: String
}
