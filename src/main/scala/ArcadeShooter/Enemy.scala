package ArcadeShooter

import org.scalajs.dom
import scala.util.Random

/**
 * Created by JordanDodson on 1/13/15.
 */
class Enemy() extends Citizen {

  val r: Int = 20

  var x: Int = Random.nextInt(500)
  var y: Int = 0

  var vx: Int = 0
  var vy: Int = 1 + Random.nextInt(15) // going down

  def expired(): Boolean = {
    x < 0 || y < 0 || x > 500 || y > 500
  }

  def spawn(): List[Citizen] = Nil

  def update(): Unit = {
    x += vx
    y += vy
  }

  def render(g: dom.CanvasRenderingContext2D): Unit = {
    g.beginPath()
    g.fillStyle = "red"
    g.fillRect(x - (r / 2), y - (r / 2), r, r)
  }
}
