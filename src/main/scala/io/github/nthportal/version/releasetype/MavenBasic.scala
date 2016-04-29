package io.github.nthportal.version
package releasetype

sealed trait MavenBasic extends ReleaseType with Ordered[MavenBasic] {
  protected val rank: Int

  override def compare(that: MavenBasic): Int = this.rank compare that.rank

  override def toString: String = extension
}

object MavenBasic {
  val defaultFactory: VersionFactory[MavenBasic] = VersionFactory({
    case Snapshot.extension => Snapshot
    case Release.extension => Release
    case s => throw new IllegalArgumentException("Invalid extension: " + s)
  },
  Release)

  object Snapshot extends MavenBasic {
    override val rank: Int = 0

    override val extension: String = "SNAPSHOT"
  }

  object Release extends MavenBasic {
    override val rank: Int = 1

    override val extension: String = "RELEASE"
  }

}
