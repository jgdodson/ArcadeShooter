package ArcadeShooter.Library

import org.scalajs.dom

// TODO: Look into methods that you can only call once. Is that a thing?
abstract class Physical(val worldWidth: Int, val worldHeight: Int) extends WorldMember {

  /**
   * The half-width of an object.
   * @return
   */
  def halfWidth: Int

  /**
   * The half-height of an object.
   * @return
   */
  def halfHeight: Int

  /**
   * The x-coordinate (pixels).
   * @return
   */
  var x: Int

  /**
   * The y-coordinate (pixels).
   * @return
   */
  var y: Int

  /**
   * The x-component of velocity (pixels / frame)
   * @return
   */
  var vx: Int

  /**
   * The y-component of velocity (pixels / frame)
   * @return
   */
  var vy: Int

  /**
   * This method determines how the object is rendered on the canvas.
   * @param g
   */
  def render(g: dom.CanvasRenderingContext2D): Unit

  /**
   * A default implementation of update for Physicals. Updates the position based
   * on the velocity.
   */
  def update(): Unit = {
    x += vx
    y += vy
  }

  /**
   * Returns true iff a collision is occurring with the other WorldMember
   * @param other
   * @return
   */
  final def isNear(other: Physical): Boolean = {
    math.abs(x - other.x) < (halfWidth + other.halfWidth) &&
      math.abs(y - other.y) < (halfHeight + other.halfHeight)
  }

}
