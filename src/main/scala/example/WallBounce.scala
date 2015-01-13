package example

import scalajs.js
import org.scalajs.dom
import scala.scalajs.js.annotation.JSExport
import scala.util.Random

/**
 * Created by JordanDodson on 1/11/15.
 */

class Ball(_r: Int, _x: Int, _y: Int, _vx: Int, _vy: Int) {

  var initAngle = Random.nextDouble * 2 * math.Pi
  var rotRate = Random.nextDouble / 5.0

  val r: Int = _r

  // Initial position
  var x: Int = _x
  var y: Int = _y

  // Initial velocity
  var vx: Int = _vx
  var vy: Int = _vy


  // One problem with using this function to determine when
  // we are near a wall is that if a ball has a large enough,
  // velocity, it can "sneak past" the wall!
  //
  // An even better way is to check if a collision will occur
  // on the upcoming turn, and then make the appropriate adjustments.
  private def nearX(v: Int): Boolean = {
    math.abs(v - x) <= r
  }

  private def nearY(h: Int): Boolean = {
    math.abs(h - y) <= r
  }

  private def outRight(bound: Int): Boolean = {
    (x + r) > bound
  }

  private def outLeft(bound: Int): Boolean = {
    (x - r) < bound
  }

  private def outTop(bound: Int): Boolean = {
    (y - r) < bound
  }

  private def outBottom(bound: Int): Boolean = {
    (y + r) > bound
  }

  // This model prevents "eternal jiggle"
  private def reflectX(f: Double): Unit = {
    vx = -(f * vx - 0.3).toInt
  }

  private def reflectY(f: Double): Unit = {
    vy = -(f * vy - 0.3).toInt
  }

  // Moves the given number of time steps
  def move(t: Int): Unit = {
    x += t * vx
    y += t * vy
  }

  // Updates the velocities given world dimensions.
  def update(left: Int, right: Int, top: Int, bottom: Int): Unit = {

    if (nearX(left) && vx < 0) reflectX(1.0)
    if (nearX(right) && vx > 0) reflectX(1.0)
    if (nearY(bottom) && vy > 0) reflectY(1.0)
    if (nearY(top) && vy < 0) reflectY(1.0)

    move(1)
  }

  def update2(left: Int, right: Int, top: Int, bottom: Int): Unit = {

    initAngle += rotRate

    if (outLeft(left) && vx < 0) reflectX(0.9)
    if (outRight(right) && vx > 0) reflectX(0.9)
    if (outBottom(bottom) && vy > 0) reflectY(0.6)
    if (outTop(top) && vy < 0) reflectY(0.9)

    move(1)

    // The ordering here matters very much! Weird bugs arise if
    // the increment is performed before the move. By incrementing the
    // velocity at the end, we are effectively  giving the propagation of
    // force a consistent duration. This makes physical sense and produces
    // the desired behavior.

    // Faux gravity
    //vy += 1

  }

}

object Ball {

  def updateAll(bs: List[Ball], left: Int, right: Int, top: Int, bottom: Int): Unit = {
    for (b <- bs) b.update2(left, right, top, bottom)
  }

  def generate(n: Int): List[Ball] = {
    var result: List[Ball] = List()
    for (i <- 1 to n) result = new Ball(10, 250, 250, Random.nextInt(10), Random.nextInt(10)) :: result
    result
  }


}

@JSExport
object WallBounce {

  @JSExport
  def main(canvas: dom.HTMLCanvasElement) = {
    val renderer = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    val size: Int = 500

    canvas.width = size
    canvas.height = size

    // This will allow the canvas to capture keyboard input.
    canvas.tabIndex = 1000
    canvas.style.outline = "none"

    val grad = renderer.createRadialGradient(size / 2, size / 2, 0, size / 2, size / 2, 350)

    grad.addColorStop(0, "blue")
    grad.addColorStop(0.5, "red")
    grad.addColorStop(0.7, "green")
    grad.addColorStop(0.85, "purple")
    grad.addColorStop(1, "white")

    renderer.strokeStyle = grad

    val balls = Ball.generate(10)

    def render(): Unit = {

      renderer.clearRect(0, 0, size, size)

      Ball.updateAll(balls, 0, size, 0, size)

      for (b <- balls) {
        renderer.beginPath()
        renderer.lineWidth = 0
        renderer.fillStyle = "blue"
        renderer.arc(b.x, b.y, b.r, b.initAngle, b.initAngle + math.Pi, anticlockwise = false)
        renderer.fill()
        renderer.beginPath()
        renderer.lineWidth = 0
        renderer.fillStyle = "blue"
        renderer.arc(b.x, b.y, b.r, b.initAngle + 0.5 * math.Pi, b.initAngle + 1.5 * math.Pi, anticlockwise = false)
        renderer.fill()

        //renderer.arc(b.x, b.y, b.r, b.initAngle, b.initAngle + 2 * math.Pi)
        //renderer.stroke()
      }

    }

    dom.setInterval(render _, 25)

    canvas.onkeydown =
      (e: dom.KeyboardEvent) => e.keyCode match {
        case 0x26 => {
          for (b <- balls) b.vy -= 2
        }
        case 0x25 => {
          for (b <- balls) b.vx -= 2
        }
        case 0x27 => {
          for (b <- balls) b.vx += 2
        }
        case 40 => {
          for (b <- balls) b.vy += 2
        }
        case 82 => {
          for (b <- balls) b.rotRate += 0.1
        }
        case 69 => {
          for (b <- balls) b.rotRate -= 0.1
        }
        // "P" scatters the balls
        case 80 => {
          for (b <- balls) {
            b.vx = math.pow(-1, Random.nextInt(2)).toInt * Random.nextInt(5)
            b.vy = math.pow(-1, Random.nextInt(2)).toInt * Random.nextInt(5)
          }
        }

      }
  }

}
