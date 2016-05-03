package io.github.nthportal.version

import io.github.nthportal.version.VersionFormatException._

/**
  * Thrown when attempting to parse an improperly formatted string as a
  * [[BaseVersion]] or [[Version]].
  *
  * @constructor Constructs a `VersionFormatException` with arguments
  *              specifying the improperly formatted version string and cause.
  * @param versionString the improperly formatted version string
  * @param cause         the cause encountered during parsing (for example,
  *                      a [[NumberFormatException]] from an improperly
  *                      formatted major, minor, or patch version number)
  */
class VersionFormatException(versionString: String, cause: Throwable)
  extends IllegalArgumentException(messagePrefix + versionString, cause) {
  /**
    * Constructs a `VersionFormatException` with an argument specifying the
    * improperly formatted version string.
    *
    * @param versionString the improperly formatted version string
    */
  def this(versionString: String) = this(versionString, null)
}

private object VersionFormatException {
  private val messagePrefix: String = "Invalid version string: "
}
