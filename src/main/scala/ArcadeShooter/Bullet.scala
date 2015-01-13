package ArcadeShooter

import org.scalajs.dom

/**
 * Created by JordanDodson on 1/13/15.
 */
class Bullet(_x: Int, _y: Int, _vxi: Int, _vyi: Int, r: Int) extends Citizen {
  var x = _x
  var y = _y
  var vx = _vxi
  var vy = _vyi

  // change this
  def expired(): Boolean = {
    (x < 0) || (y < 0) || (x > 500) || (y > 500)
  }

  def update(): Unit = {
    x += vx
    y += vy
  }

  def spawn(): List[Citizen] = Nil

  def isNear(otherX: Int, otherY: Int, otherR: Int): Boolean = {
    ((x - otherX).abs + (y - otherY).abs) < (r + otherR)
  }

  def render(g: dom.CanvasRenderingContext2D): Unit = {
    g.beginPath()
    g.fillStyle = "black"
    g.arc(x, y, r, 0, 2 * math.Pi, anticlockwise = false)
    g.fill()
  }
}


