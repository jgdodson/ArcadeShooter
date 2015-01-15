package ArcadeShooter.Examples.TestGame

import ArcadeShooter.Library.Shooter
import org.scalajs.dom

// For arcade-style games. (i.e. Galaga)
// A walker is a user-controlled agent that walks across the bottom of the canvas.
class Walker(_pace: Int, val halfHeight: Int, val halfWidth: Int, worldWidth: Int, worldHeight: Int) extends Shooter(worldWidth, worldHeight) {

  var numShotsFired = 0

  // Pace of the walker, that is, the velocity when walking left or right.
  val pace: Int = math.abs(_pace)

  // Position
  var x: Int = worldWidth / 2
  var y: Int = worldHeight - (halfHeight)

  // Velocity
  var vx: Int = 0
  var vy: Int = 0

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
  override def update(): Unit = {

    // If we're stopping and moving, slowdown.
    if (stopping && math.abs(vx) > 0) reduceVel(1)

    // Update position
    x += vx
  }

  def onKeyDown(e: dom.KeyboardEvent): Unit = e.keyCode match {
    // moving left/right
    case 37 => startMovingLeft()
    case 39 => startMovingRight()

    // shooting commands
    case 87 => fireSingle()
    case 69 => fireTriple()
    case 81 => shockWave()
  }

  def onKeyUp(e: dom.KeyboardEvent): Unit = e.keyCode match {
    // moving left/right
    case 37 => stopMovingLeft()
    case 39 => stopMovingRight()
  }

  def render(g: dom.CanvasRenderingContext2D): Unit = {
    g.beginPath()
    g.fillStyle = "black"
    g.fillRect(x - halfWidth, y - halfHeight, 2 * halfWidth, 2 * halfHeight)
  }

  def fireSingle(): Unit = {
    nextShot = 1
  }

  def fireTriple(): Unit = {
    nextShot = 2
  }

  def shockWave(): Unit = {
    nextShot = 3
  }

  override def spawn(): List[Bullet] = {

    // We will match on the current state of the nextShot flag.
    val toShoot = nextShot

    // Always reset for the next update.
    nextShot = 0

    toShoot match {
      case 1 =>
        numShotsFired += 1
        List(new Bullet(x, y - halfHeight, 0, -10, 5, worldWidth, worldHeight))

      case 2 =>
        numShotsFired += 3
        for (i <- List.range(-2, 4, 2)) yield new Bullet(x, y - halfHeight, i, -10, 5, worldWidth, worldHeight)

      case 3 =>
        numShotsFired += 20
        for (i <- List.range(0, 21)) yield new Bullet(x, y + halfHeight,
          (250 * math.cos((i / 20.0) * math.Pi)).toInt / 15,
          -(250 * math.sin((i / 20.0) * math.Pi)).toInt / 15, 5, worldWidth, worldHeight)

      case _ => Nil
    }
  }
}

