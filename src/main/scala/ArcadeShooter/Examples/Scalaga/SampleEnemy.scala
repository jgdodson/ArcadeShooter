package ArcadeShooter.Examples.Scalaga

import ArcadeShooter.Library.Enemy
import org.scalajs.dom

import scala.util.Random

class SampleEnemy(r: Int, worldWidth: Int, worldHeight: Int) extends Enemy(worldWidth, worldHeight) {

  val halfWidth: Int = r
  val halfHeight: Int = r

  var x: Int = Random.nextInt(worldWidth)
  var y: Int = 0

  var vx: Int = 0
  var vy: Int = 1 + Random.nextInt(15) // going down

  def spawn() = Nil

  def render(g: dom.CanvasRenderingContext2D): Unit = {

    g.fillStyle = "red"
    g.lineWidth = 3
    g.strokeStyle = "silver"

    g.beginPath()
    g.moveTo(x - halfWidth,y - halfHeight)
    g.lineTo(x,y + halfWidth)
    g.lineTo(x + halfWidth, y - halfHeight)
    g.closePath()

    g.fill()
    g.stroke()
  }
}