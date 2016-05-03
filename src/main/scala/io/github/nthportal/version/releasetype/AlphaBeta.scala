package io.github.nthportal.version
package releasetype

/**
  * Represents a release cycle with pre-alpha, alpha, beta, release candidate,
  * and final release stages. There may be an arbitrary number of release candidates.
  */
sealed trait AlphaBeta extends ReleaseType with Ordered[AlphaBeta] {
  protected val rank: Int

  override def compare(that: AlphaBeta): Int = this.rank compare that.rank

  /**
    * @see [[extension]]
    *
    * @return the [[extension]] for this release type
    */
  override def toString: String = extension
}

object AlphaBeta {
  private val rcPrefix = "rc."

  /**
    * The default factory for creating `[[Version]]`s with the [[AlphaBeta]]
    * release type. The factory has a function for parsing release types from
    * a string, and a default release type of [[Release]].
    */
  val defaultFactory: VersionFactory[AlphaBeta] = VersionFactory({
    case PreAlpha.extension => PreAlpha
    case Alpha.extension => Alpha
    case Beta.extension => Beta
    case Release.extension => Release
    case s if s.startsWith(rcPrefix + "0") =>
      val rcNum = s.substring(rcPrefix.length)
      if (rcNum.length == 1) ReleaseCandidate(0)
      else throw new IllegalArgumentException("Release candidate number cannot be hex or octal (starts with '0')")
    case s if s.startsWith(rcPrefix) =>
      val i = try {
        Integer.parseInt(s.substring(rcPrefix.length))
      } catch {
        case e: NumberFormatException => throw new IllegalArgumentException("Invalid release candidate number: " + s)
      }
      ReleaseCandidate(i)
    case s => throw new IllegalArgumentException("Invalid extension: " + s)
  },
  Release)

  /**
    * Represents the `n`th release candidate, where `n` is the [[candidateNumber]].
    *
    * @constructor Constructs a `ReleaseCandidate` with the specified release
    *              candidate number.
    * @param candidateNumber the number of the release candidate
    */
  final case class ReleaseCandidate(candidateNumber: Int) extends AlphaBeta {
    if (candidateNumber < 0) throw new IllegalArgumentException("Release candidate number cannot be negative")

    override protected val rank: Int = 3

    /** @inheritdoc */
    override def extension: String = rcPrefix + candidateNumber

    override def compare(that: AlphaBeta): Int = that match {
      case other: ReleaseCandidate => this.candidateNumber compare other.candidateNumber
      case _ => super.compare(that)
    }
  }

  /**
    * Represents a pre-alpha release.
    */
  object PreAlpha extends AlphaBeta {
    override protected val rank: Int = 0

    override val extension: String = "pre-alpha"
  }

  /**
    * Represents an alpha release.
    */
  object Alpha extends AlphaBeta {
    override protected val rank: Int = 1

    override val extension: String = "alpha"
  }

  /**
    * Represents a beta release.
    */
  object Beta extends AlphaBeta {
    override protected val rank: Int = 2

    override val extension: String = "beta"
  }

  /**
    * Represents a final release.
    */
  object Release extends AlphaBeta {
    override protected val rank: Int = 4

    override val extension: String = "release"
  }

}
