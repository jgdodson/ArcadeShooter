package example

import scalajs.js
import org.scalajs.dom
import scala.scalajs.js.annotation.JSExport
import scala.util.Random

/**
 * Created by JordanDodson on 1/11/15.
 */

class StickPerson {
  var x: Int = 250

  var vx: Int = 0

  def moveRight(n: Int): Unit = {
    vx += n
  }

  def moveLeft(n: Int): Unit = {
    vx -= n
  }

  def update(): Unit = {
    x += vx

    vx = (vx * 0.99).toInt
  }
}


trait Citizen {
  def expired(): Boolean

  def render(g: dom.CanvasRenderingContext2D): Unit

  def spawn(): List[Citizen]

  def update(): Unit
}

class Bullet(_x: Int, _y: Int, _vxi: Int, _vyi: Int) extends Citizen {
  var x = _x
  var y = _y
  var vx = _vxi
  var vy = _vyi

  def expired(): Boolean = {
    (x < 0) || (y < 0) || (x > 500) || (y > 500)
  }

  def update(): Unit = {
    x += vx
    y += vy
  }

  def spawn(): List[Citizen] = Nil

  def render(g: dom.CanvasRenderingContext2D): Unit = {
    g.beginPath()
    g.arc(x, y, 5, 0, 2 * math.Pi, anticlockwise = false)
    g.fill()
  }

}

// A fading trail could be implemented using a
// queue
class Walker(_pace: Int, _length: Int, _width: Int) extends Citizen {

  val length: Int = _length
  val width: Int = _width

  // Pace of the walker
  val pace: Int = math.abs(_pace)

  // Position
  var x: Int = 250
  var y: Int = 490

  // Velocity
  var vx: Int = 0

  var movingRight: Boolean = false
  var movingLeft: Boolean = false
  var stopping: Boolean = false

  def startMovingRight(): Unit = {
    if (!movingRight) {
      vx = pace
      movingRight = true
      movingLeft = false
      stopping = false
    }
  }

  def startMovingLeft(): Unit = {
    if (!movingLeft) {
      vx = -pace
      movingLeft = true
      movingRight = false
      stopping = false
    }
  }

  def stopMoving(): Unit = {
    stopping = true
    movingRight = false
    movingLeft = false
  }

  private def reduceVel(n: Int): Unit = {
    vx = math.signum(vx) * (math.abs(vx) - n)
  }

  // Idea: Use a "future stack" to handle effects that unfold over the coming frames.
  def update(): Unit = {
    if (stopping && math.abs(vx) > 0) reduceVel(1)
    x += vx
  }

  def spawn(): List[Citizen] = Nil

  // never expires
  def expired(): Boolean = false

  def render(g: dom.CanvasRenderingContext2D): Unit = {
    g.beginPath()
    g.fillRect(x - (width / 2), 500 - length, width, length)
  }
}

class Shooter extends Walker(10, 50, 30) {

  private var nextShot: Int = 0

  def fireSingle(): Unit = {
    nextShot = 1
  }

  def fireTriple(): Unit = {
    nextShot = 2
  }

  def shockWave(): Unit = {
    nextShot = 3
  }


  // Idea, have the update method return the spawn list...
  // Nah, keep it modular.
  override def spawn(): List[Citizen] = {
    nextShot match {
      case 1 =>
        nextShot = 0
        List(new Bullet(x, 500 - length, 0, -10))

      case 2 =>
        nextShot = 0
        for (i <- List.range(-2, 4, 2)) yield new Bullet(x, 500 - length, i, -10)

      case 3 =>
        nextShot = 0
        for (i <- List.range(0, 21)) yield new Bullet(x, 500, (40 * math.cos((i / 20.0) * math.Pi)).toInt / 3, -(40 * math.sin((i / 20.0) * math.Pi)).toInt / 3)

      case _ => Nil


    }
  }

}

class World(_citizens: List[Citizen]) {

  var citizens: List[Citizen] = _citizens

  def updateAndRender(g: dom.CanvasRenderingContext2D): Unit = {
    var result: List[Citizen] = Nil
    for (c <- citizens) {
      if (!c.expired()) {

        c.render(g)

        // Update all of the citizens of the world.
        c.update() // doh! - I forgot this line for just a moment, but careful thought quickly revealed the error.

        // The next generation includes the spawn of this generation.
        result = c :: c.spawn() ::: result
      }
    }
    citizens = result
  }

}


@JSExport
object StickMan {

  @JSExport
  def main(canvas: dom.HTMLCanvasElement): Unit = {
    val renderer = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    val size = 500

    canvas.tabIndex = 1000
    canvas.width = size
    canvas.height = size
    canvas.style.outline = "none"

    val wakka = new Shooter
    val world: World = new World(List(wakka))

    def renderWorld(): Unit = {
      renderer.clearRect(0, 0, size, size)
      world.updateAndRender(renderer)
    }

    dom.setInterval(renderWorld _, 20)

    canvas.onkeydown = (e: dom.KeyboardEvent) => e.keyCode match {
      // moving left/right
      case 37 => wakka.startMovingLeft()
      case 39 => wakka.startMovingRight()

      // shooting commands
      case 87 => wakka.fireSingle()
      case 69 => wakka.fireTriple()
      case 81 => wakka.shockWave()
    }

    canvas.onkeyup = (e: dom.KeyboardEvent) => e.keyCode match {
      // moving left/right
      case 37 => wakka.stopMoving()
      case 39 => wakka.stopMoving()
    }


  }

}
