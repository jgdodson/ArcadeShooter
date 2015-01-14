package ArcadeShooter

import org.scalajs.dom
import scala.util.Random

class Enemy(r: Int, worldWidth: Int, worldHeight: Int) extends Citizen {

  val halfWidth: Int = r
  val halfLength: Int = r

  var x: Int = Random.nextInt(worldWidth)
  var y: Int = 0

  var vx: Int = 0
  var vy: Int = 1 + Random.nextInt(15) // going down

  override def expired(): Boolean = {
    x < 0 || y < 0 || x > worldWidth || y > worldHeight
  }

  def render(g: dom.CanvasRenderingContext2D): Unit = {

    g.fillStyle = "red"
    g.lineWidth = 3
    g.strokeStyle = "silver"

    g.beginPath()
    g.moveTo(x - halfWidth,y - halfLength)
    g.lineTo(x,y + halfWidth)
    g.lineTo(x + halfWidth, y - halfLength)
    g.closePath()

    g.fill()
    g.stroke()
  }
}