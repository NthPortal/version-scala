package io.github.nthportal.version

final class VersionFactory[T <: ReleaseType with Ordered[T]] private(releaseTypeFromStr: String => T,
                                                                     isDefaultReleaseType: T => Boolean,
                                                                     defaultReleaseType: Option[T]) {
  private val ordering: Ordering[Version[T]] = Ordering.by(v => (v.base, v.releaseType))

  def this(releaseTypeFromStr: String => T, defaultReleaseType: T) =
    this(releaseTypeFromStr, (t: T) => t == defaultReleaseType, Some(defaultReleaseType))

  def this(releaseTypeFromStr: String => T) = this(releaseTypeFromStr, (t: T) => false, None)

  def this(defaultReleaseType: T) =
    this(VersionFactory.parsingNotSupported.asInstanceOf[(String => T)], defaultReleaseType)

  def this() = this(VersionFactory.parsingNotSupported.asInstanceOf[(String => T)])

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

  def newVersion(core: BaseVersion, releaseType: T): Version[T] =
    Version(core, releaseType, isDefaultReleaseType(releaseType), ordering)

  def apply(core: BaseVersion, releaseType: T): Version[T] = newVersion(core, releaseType)
}

object VersionFactory {
  private val parsingNotSupported: (String => Any) =
    (_: String) => throw new UnsupportedOperationException("Factory only supports parsing version from string for " +
      "default release type")

  def apply[T <: ReleaseType with Ordered[T]](releaseTypeFromStr: String => T, defaultReleaseType: T) =
    new VersionFactory(releaseTypeFromStr, defaultReleaseType)

  def apply[T <: ReleaseType with Ordered[T]](releaseTypeFromStr: String => T) = new VersionFactory(releaseTypeFromStr)

  def apply[T <: ReleaseType with Ordered[T]](defaultReleaseType: T) = new VersionFactory(defaultReleaseType)

  def apply[T <: ReleaseType with Ordered[T]]() = new VersionFactory()
}
