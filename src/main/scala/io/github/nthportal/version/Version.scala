package io.github.nthportal.version

/**
  * Represents a [[http://semver.org/ SemVer]] version and a release type of the
  * form `x.x.x[-release_type]`; for example, `1.3.0-SNAPSHOT`. If the release
  * type is the default release type (usually a final release), then it represents
  * a version of the type `x.x.x`, but still contains the information about the
  * release type.
  *
  * NOTE: Versions created by different `[[VersionFactory]]`s are not guaranteed
  * to evaluate as equal or provide the same hash code even with the same [[base]]
  * and [[releaseType]]. Specifically, if the factories had different default
  * build types, then the versions may evaluate as unequal.
  *
  * @param base        a [[BaseVersion]] describing the `x.x.x` part of
  *                    `x.x.x[-release_type]`
  * @param releaseType the `release-type` part of `x.x.x[-release_type]`; if it
  *                    is the default release type, then [[toString]] returns
  *                    `x.x.x` rather than `x.x.x-release_type`
  * @param defaultType whether or not [[releaseType]] is the default release type
  * @param ordering    an ordering defining how to sort `Version`s with type
  *                    parameter [[T]]
  * @tparam T the `type` of [[releaseType]]
  */
final case class Version[T <: ReleaseType with Ordered[T]] private[version](base: BaseVersion,
                                                                            releaseType: T,
                                                                            private val defaultType: Boolean,
                                                                            private val ordering: Ordering[Version[T]])
  extends Ordered[Version[T]] {

  override def compare(that: Version[T]): Int = ordering.compare(this, that)

  /**
    * Returns this version formatted as `x.x.x-release_type` if the
    * [[releaseType]] is not the default release type; `x.x.x` if it is.
    *
    * @return this version represented as a string
    */
  override def toString: String = base + (if (defaultType) "" else "-" + releaseType.extension)
}
