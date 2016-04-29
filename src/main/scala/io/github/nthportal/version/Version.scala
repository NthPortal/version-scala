package io.github.nthportal.version

/**
  * Versions created by different [[VersionFactory]]s are not guaranteed to evaluate
  * as equal or provide the same hash code even with the same core and buildType.
  * Specifically, if the factories had different default build types, then the
  * versions may evaluate as unequal.
  *
  * @param core
  * @param releaseType
  * @param defaultType
  * @tparam T
  */
final case class Version[T <: ReleaseType with Ordered[T]] private[version](core: CoreVersion,
                                                                            releaseType: T,
                                                                            private val defaultType: Boolean,
                                                                            ordering: Ordering[Version[T]]) extends Ordered[Version[T]] {
  override def compare(that: Version[T]): Int = ordering.compare(this, that)

  override def toString: String = core + (if (defaultType) "" else "-" + releaseType.extension)
}
