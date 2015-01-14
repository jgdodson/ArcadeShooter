package ArcadeShooter

import org.scalajs.dom

class Bullet(var x: Int,
             var y: Int,
             var vx: Int,
             var vy: Int,
             r: Int,
             worldWidth: Int,
             worldHeight: Int) extends Citizen {

  val halfLength: Int = r
  val halfWidth: Int = r

  override def expired(): Boolean = {
    (x < 0) || (y < 0) || (x > worldWidth) || (y > worldHeight)
  }

  def render(g: dom.CanvasRenderingContext2D): Unit = {
    g.beginPath()
    g.fillStyle = "black"
    g.arc(x, y, r, 0, 2 * math.Pi, anticlockwise = false)
    g.fill()
  }
}


