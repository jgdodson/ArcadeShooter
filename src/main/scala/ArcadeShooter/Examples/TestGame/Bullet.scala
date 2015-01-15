package ArcadeShooter.Examples.TestGame

import ArcadeShooter.Library.Projectile
import org.scalajs.dom

class Bullet(var x: Int,
             var y: Int,
             var vx: Int,
             var vy: Int,
             r: Int,
             worldWidth: Int,
             worldHeight: Int) extends Projectile(worldWidth, worldHeight) {

  val halfHeight: Int = r
  val halfWidth: Int = r

  def spawn() = Nil

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


