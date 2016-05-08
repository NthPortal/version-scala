package io.github.nthportal.version
package releasetype

/**
  * Represents basic (Apache) Maven release types: snapshot and final release.
  */
sealed trait MavenBasic extends ReleaseType with Ordered[MavenBasic] {
  protected val rank: Int

  override def compare(that: MavenBasic): Int = this.rank compare that.rank

  /**
    * @see [[extension]]
    *
    * @return the [[extension]] for this release type
    */
  override def toString: String = extension
}

object MavenBasic {
  /**
    * The default factory for creating `[[Version]]`s with the [[MavenBasic]]
    * release type. The factory has a function for parsing release types from
    * a string, and a default release type of [[Release]].
    */
  val defaultFactory: VersionFactory[MavenBasic] = VersionFactory({
    case Snapshot.extension => Snapshot
    case s => throw new IllegalArgumentException("Invalid extension: " + s)
  },
  Release)

  /**
    * Represents a Maven snapshot release.
    */
  object Snapshot extends MavenBasic {
    override val rank: Int = 0

    override val extension: String = "SNAPSHOT"
  }

  /**
    * Represents a final Maven release.
    */
  object Release extends MavenBasic {
    override val rank: Int = 1

    override val extension: String = "RELEASE"
  }

}
