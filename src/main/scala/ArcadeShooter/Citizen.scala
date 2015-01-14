package ArcadeShooter

import org.scalajs.dom

// DONE: Need to add a isNear(c: Citizen) method to this list.
//  Would first need to decide on what the convention for determining
//  proximity is. Use (x,y) or use radius?


// DONE: Move x,y coords into this trait
//  Could then implement the same isNear() method for all subclasses (make it final)

trait Citizen {

  // The half-width and the half-length.
  val halfWidth: Int
  val halfLength: Int

  var x: Int
  var y: Int

  def vx: Int
  def vy: Int

  def expired(): Boolean

  def render(g: dom.CanvasRenderingContext2D): Unit

  def spawn(): List[Citizen] = Nil

  final def isNear(other: Citizen): Boolean = {
    math.abs(x - other.x) < (halfWidth + other.halfWidth) &&
    math.abs(y - other.y) < (halfLength + other.halfLength)
  }

  def update(): Unit = {
    x += vx
    y += vy
  }
}
