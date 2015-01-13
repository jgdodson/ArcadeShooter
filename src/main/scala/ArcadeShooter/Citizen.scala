package ArcadeShooter

import org.scalajs.dom

/**
 * Created by JordanDodson on 1/13/15.
 */
trait Citizen {
  def expired(): Boolean

  def render(g: dom.CanvasRenderingContext2D): Unit

  def spawn(): List[Citizen]

  def update(): Unit
}
