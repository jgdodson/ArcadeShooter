package ArcadeShooter.Library

abstract class WorldMember {

  /**
   * Returns true iff the WorldMember can be removed from the world.
   * @return
   */
  def expired(): Boolean

  /**
   * Returns the WorldMembers that should be added to the world
   * during the next cycle.
   * @return
   */
  def spawn(): List[WorldMember]

  /**
   * Performs any evolution required from one cycle to the next.
   */
  def update(): Unit
}
