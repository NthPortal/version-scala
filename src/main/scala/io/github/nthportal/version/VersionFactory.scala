package io.github.nthportal.version

import java.util.function.Function

/**
  * A factory used for creating `[[Version]]`s with a given [[ReleaseType]].
  *
  * @param releaseTypeFromStr   a function which converts a release type extension string
  *                             into a [[ReleaseType]]
  * @param isDefaultReleaseType a predicate which determines if a [[ReleaseType]] is the
  *                             default release type
  * @param defaultReleaseType   an [[Option]] containing the default release type, if one
  *                             is provided
  * @tparam T the `type` of the [[ReleaseType]] of `[[Version]]`s created by this factory
  */
final class VersionFactory[T <: ReleaseType with Ordered[T]] private(releaseTypeFromStr: String => T,
                                                                     isDefaultReleaseType: T => Boolean,
                                                                     defaultReleaseType: Option[T]) {
  private val ordering: Ordering[Version[T]] = Ordering.by(v => (v.base, v.releaseType))

  /**
    * Constructs a `VersionFactory` which the specified function for converting a release
    * type extension (`release_type` in `x.x.x-release_type`) to a `[[ReleaseType]]`, and
    * the specified default release type. The function should throw an
    * [[IllegalArgumentException]] if the extension is invalid.
    *
    * @param releaseTypeFromStr a function which converts a release type extension string
    *                           into a [[ReleaseType]]
    * @param defaultReleaseType the [[ReleaseType]] for which an extension should not be
    *                           appended to the version `x.x.x`
    */
  def this(releaseTypeFromStr: String => T, defaultReleaseType: T) =
    this(releaseTypeFromStr, (t: T) => t == defaultReleaseType, Some(defaultReleaseType))

  /**
    * Constructs a `VersionFactory` with no default release type which uses the specified
    * function for converting a release type extension (`release_type` in `x.x.x-release_type`)
    * to a `[[ReleaseType]]`. The function should throw an [[IllegalArgumentException]] if
    * the extension is invalid.
    *
    * @param releaseTypeFromStr a function which converts a release type extension string
    *                           into a [[ReleaseType]]
    */
  def this(releaseTypeFromStr: String => T) = this(releaseTypeFromStr, (t: T) => false, None)

  /**
    * Constructs a `VersionFactory` with the specified default release type which does not
    * support parsing `[[Version]]`s from strings.
    *
    * ''Note: Due to how versions are parsed, this factory'' '''will''' ''in fact support
    * parsing a version string with no release type extension (''`x.x.x`'') into a
    * `[[Version]]` with the default release type. This behaviour is present due to the
    * fact that it would be significantly more complicated to forbid it. However, it
    * should be considered a bug, and'' '''SHOULD NOT''' ''be considered part of the API.
    * The behaviour may be removed at an arbitrary time in the future without warning.''
    *
    * @param defaultReleaseType the [[ReleaseType]] for which an extension should not be
    *                           appended to the version `x.x.x`
    */
  def this(defaultReleaseType: T) =
    this(VersionFactory.parsingNotSupported.asInstanceOf[(String => T)], defaultReleaseType)

  /**
    * Constructs a `VersionFactory` with no default release type, and which does not
    * support parsing `[[Version]]`s from strings.
    */
  def this() = this(VersionFactory.parsingNotSupported.asInstanceOf[(String => T)])

  /**
    * Parses a version string (`x.x.x[-release_type]`) into a [[Version]].
    *
    * @param version a string in the format `x.x.x` or `x.x.x-release_type` to parse into
    *                a [[Version]]
    * @throws VersionFormatException        if the version string is improperly formatted
    * @throws UnsupportedOperationException if this factory does not support parsing from
    *                                       strings, or if the version string has no release
    *                                       type extension (i.e. it is formatted as `x.x.x`),
    *                                       and this factory does not have a default release
    *                                       type
    * @return a [[Version]] represented by the version string
    */
  @throws[VersionFormatException]
  @throws[UnsupportedOperationException]
  def parseVersion(version: String): Version[T] = {
    version.split("-", 2) match {
      case Array(v) =>
        if (defaultReleaseType.isDefined) newVersion(BaseVersion.parseVersion(v), defaultReleaseType.get)
        else throw new UnsupportedOperationException("Cannot parse version: " + v + "; no default release type defined")
      case Array(v, ext) =>
        try {
          newVersion(BaseVersion.parseVersion(v), releaseTypeFromStr(ext))
        } catch {
          case e: UnsupportedOperationException => throw e
          case e: Throwable => throw new VersionFormatException(version, e)
        }
    }
  }

  /**
    * Functional wrapper around [[newVersion]].
    */
  def apply(base: BaseVersion, releaseType: T): Version[T] = newVersion(base, releaseType)

  /**
    * Creates a new [[Version]] with the specified [[BaseVersion]] and [[ReleaseType]]
    *
    * @param base        the [[BaseVersion]] part of the version
    * @param releaseType the [[ReleaseType]] of the version
    * @return a new version with the specified base and release type
    */
  def newVersion(base: BaseVersion, releaseType: T): Version[T] =
    Version(base, releaseType, isDefaultReleaseType(releaseType), ordering)
}

object VersionFactory {
  private val parsingNotSupported: (String => Any) =
    (_: String) => throw new UnsupportedOperationException("Factory only supports parsing version from string for " +
      "default release type")

  def apply[T <: ReleaseType with Ordered[T]](releaseTypeFromStr: String => T, defaultReleaseType: T) =
    new VersionFactory(releaseTypeFromStr, defaultReleaseType)

  def apply[T <: ReleaseType with Ordered[T]](releaseTypeFromStr: String => T) = new VersionFactory(releaseTypeFromStr)

  def apply[T <: ReleaseType with Ordered[T]](defaultReleaseType: T) = new VersionFactory(defaultReleaseType)

  def apply[T <: ReleaseType with Ordered[T]]() = new VersionFactory[T]

  /* For ease of use in Java */

  def of[T <: ReleaseType with Ordered[T]](releaseTypeFromStr: Function[String, T], defaultReleaseType: T) =
    new VersionFactory(releaseTypeFromStr(_), defaultReleaseType)

  def of[T <: ReleaseType with Ordered[T]](releaseTypeFromStr: Function[String, T]) =
    new VersionFactory(releaseTypeFromStr(_))
}
