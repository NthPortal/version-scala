package io.github.nthportal.version
package releasetype

sealed trait AlphaBeta extends ReleaseType with Ordered[AlphaBeta] {
  protected val rank: Int

  override def compare(that: AlphaBeta): Int = this.rank compare that.rank

  override def toString: String = extension
}

object AlphaBeta {
  private val releaseCandidatePrefix = "rc."

  val defaultFactory: VersionFactory[AlphaBeta] = VersionFactory({
    case PreAlpha.extension => PreAlpha
    case Alpha.extension => Alpha
    case Beta.extension => Beta
    case Release.extension => Release
    case s if s.startsWith(releaseCandidatePrefix + "0") =>
      val rcNum = s.substring(releaseCandidatePrefix.length)
      if (rcNum.length == 1) ReleaseCandidate(0)
      else throw new IllegalArgumentException("Release candidate number cannot be hex or octal (starts with '0')")
    case s if s.startsWith(releaseCandidatePrefix) =>
      val i = try {
        Integer.parseInt(s.substring(releaseCandidatePrefix.length))
      } catch {
        case e: NumberFormatException => throw new IllegalArgumentException("Invalid release candidate number: " + s)
      }
      ReleaseCandidate(i)
    case s => throw new IllegalArgumentException("Invalid extension: " + s)
  },
  Release)

  final case class ReleaseCandidate(candidateNumber: Int) extends AlphaBeta {
    if (candidateNumber < 0) throw new IllegalArgumentException("Release candidate number cannot be negative")

    override protected val rank: Int = 3

    override def extension: String = releaseCandidatePrefix + candidateNumber

    override def compare(that: AlphaBeta): Int = that match {
      case other: ReleaseCandidate => this.candidateNumber compare other.candidateNumber
      case _ => super.compare(that)
    }
  }

  object PreAlpha extends AlphaBeta {
    override protected val rank: Int = 0

    override val extension: String = "pre-alpha"
  }

  object Alpha extends AlphaBeta {
    override protected val rank: Int = 1

    override val extension: String = "alpha"
  }

  object Beta extends AlphaBeta {
    override protected val rank: Int = 2

    override val extension: String = "beta"
  }

  object Release extends AlphaBeta {
    override protected val rank: Int = 4

    override val extension: String = "release"
  }

}
