package ArcadeShooter.Library

import org.scalajs.dom

abstract class Shooter(worldWidth: Int, worldHeight: Int) extends Physical(worldWidth, worldHeight) {

  def numShotsFired: Int

  protected var nextShot: Int = 0

  /**
   * Determines what happens when the user presses a key.
   * @param e
   */
  def onKeyUp(e: dom.KeyboardEvent): Unit

  /**
   * Determines what happens when the user releases a key.
   * @param e
   */
  def onKeyDown(e: dom.KeyboardEvent): Unit

  def expired(): Boolean = false

  /**
   * Shooters can only spawn Projectiles.
   * @return
   */
  def spawn(): List[Projectile]

}
