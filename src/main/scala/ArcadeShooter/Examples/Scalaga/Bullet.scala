package ArcadeShooter.Examples.Scalaga

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

    // Setup
    val squareSize = 2
    var currX = x - halfWidth
    var currY = y - halfHeight

    val pattern =
    "nnrrnne" +
    "nrrwrne" +
    "nrwwrne" +
    "nnwwnne" +
    "nnwwnne" +
    "nnwwnne" +
    "nnwwnne"

    def handleSquare(c: Char): Unit = c match {
      case 'e' =>
        currX -= 6 * squareSize
        currY += squareSize
      case 'w' =>
        g.fillStyle = "white"
        g.fillRect(currX, currY, squareSize, squareSize)
        currX += squareSize
      case 'r' =>
        g.fillStyle = "red"
        g.fillRect(currX, currY, squareSize, squareSize)
        currX += squareSize
      case 'b' =>
        g.fillStyle = "blue"
        g.fillRect(currX, currY, squareSize, squareSize)
        currX += squareSize
      case _ => currX += squareSize
    }

    // Do the work
    for {c <- pattern} handleSquare(c)
  }
}


