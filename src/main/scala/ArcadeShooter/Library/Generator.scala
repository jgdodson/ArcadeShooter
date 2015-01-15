package ArcadeShooter.Library

abstract class Generator extends NonPhysical {

  /**
   * Generators can only spawn Physicals.
   * @return
   */
  def spawn(): List[Enemy]
}
