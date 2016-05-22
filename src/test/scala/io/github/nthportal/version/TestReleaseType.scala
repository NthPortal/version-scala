package io.github.nthportal.version

sealed trait TestReleaseType extends ReleaseType with Ordered[TestReleaseType] {
  val rank: Int

  override def compare(that: TestReleaseType): Int = this.rank compare that.rank

  override def toString: String = extension
}

object TestReleaseType {
  val basicFactory = VersionFactory[TestReleaseType](Default)

  object Greater extends TestReleaseType {
    override val rank: Int = 2
    override val extension = "Greater"
  }

  object Default extends TestReleaseType {
    override val rank: Int = 1
    override val extension = ""
  }

  object Lesser extends TestReleaseType {
    override val rank: Int = 0
    override val extension = "Lesser"
  }

}
