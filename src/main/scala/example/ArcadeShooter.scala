package example

// TODO: Refactor into multiple files
// TODO: Add method to World class that introduces citizens into world
// TODO: Add collision detection. (keep separate list of bullets and enemies?)
// TODO: Add infobars (ammo, lives, etc.)

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

  // change this
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

// A walker is a user-controlled agent that walks across the bottom of the canvas. Used
// for arcade-style games. (i.e. Galaga)
class Walker(_pace: Int, _length: Int, _width: Int) extends Citizen {

  // The length of the Walker in pixels.
  val length: Int = _length

  // The width of the walker in pixels.
  val width: Int = _width

  // Pace of the walker, that is, the velocity when walking left or right.
  val pace: Int = math.abs(_pace)

  // Position
  var x: Int = 250
  var y: Int = 500 - (length / 2)

  // Velocity
  var vx: Int = 0

  // Flags indicating the current state of the Walker.
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


  // Subtly wrong from a UI point of view.
  def stopMoving(): Unit = {
    stopping = true
    movingRight = false
    movingLeft = false
  }

  // Stops moving right, only if the Walker was already moving right.
  def stopMovingRight(): Unit = {
    if (movingRight) {
      movingRight = false
      stopping = true
    }
  }

  // Stops moving left, only if the Walker was already moving left.
  def stopMovingLeft(): Unit = {
    if (movingLeft) {
      movingLeft = false
      stopping = true
    }
  }

  private def reduceVel(n: Int): Unit = {
    vx = math.signum(vx) * (math.abs(vx) - n)
  }

  // Idea: Use a "future stack" to handle effects that unfold over the coming frames.
  def update(): Unit = {

    // If we're stopping and moving, slowdown.
    if (stopping && math.abs(vx) > 0) reduceVel(1)

    // Update position
    x += vx
  }

  // Walkers do not spawn anything.
  def spawn(): List[Citizen] = Nil

  // never expires
  def expired(): Boolean = false

  def render(g: dom.CanvasRenderingContext2D): Unit = {
    g.beginPath()
    g.fillRect(x - (width / 2), y - (length / 2), width, length)
  }
}

// Might not need a class for this anymore, since all citizens have a spawn method.
// Instead, have an object that contains a collection of different style shooters.
class Shooter extends Walker(10, 50, 30) {

  // A flag indicating what the Shooter should spawn(fire) during the next update.
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


  override def spawn(): List[Citizen] = {

    // We will match on the current state of the nextShot flag.
    val toShoot = nextShot

    // Always reset for the next update.
    nextShot = 0

    toShoot match {
      case 1 => List(new Bullet(x, y - (length / 2), 0, -10))

      case 2 => for (i <- List.range(-2, 4, 2)) yield new Bullet(x, y - (length / 2), i, -10)

      case 3 => for (i <- List.range(0, 21)) yield new Bullet(x, y + (length / 2), (250 * math.cos((i / 20.0) * math.Pi)).toInt / 15, -(250 * math.sin((i / 20.0) * math.Pi)).toInt / 15)

      case _ => Nil
    }
  }

}

class World(_citizens: List[Citizen]) {

  // A list of all the citizens in the world.
  var citizens: List[Citizen] = _citizens

  def updateAndRender(g: dom.CanvasRenderingContext2D): Unit = {
    var result: List[Citizen] = Nil
    for (c <- citizens) {

      // If a citizen is expired, we remove it from the world.
      if (!c.expired()) {

        // Render needs to come before the update.
        c.render(g)

        // Update all of the citizens of the world.
        c.update()

        // The next generation includes the spawn of this generation.
        result = c :: c.spawn() ::: result
      }
    }

    // Update the citizens of the world.
    citizens = result
  }

}


@JSExport
object ArcadeShooter {

  @JSExport
  def main(canvas: dom.HTMLCanvasElement): Unit = {
    val renderer = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    val size = 500

    canvas.tabIndex = 1000
    canvas.width = size
    canvas.height = size
    canvas.style.outline = "none"

    val wakka = new Shooter


    // Idea: provide the world class with methods for adding citizens. This
    // will make it easier to inform the citizens of certain world-properties.

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
      case 37 => wakka.stopMovingLeft()
      case 39 => wakka.stopMovingRight()
    }


  }

}
