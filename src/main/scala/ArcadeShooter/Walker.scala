package ArcadeShooter

import org.scalajs.dom

// For arcade-style games. (i.e. Galaga)
// A walker is a user-controlled agent that walks across the bottom of the canvas.
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
  val vy: Int = 0

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

  // never expires
  def expired(): Boolean = false

  def render(g: dom.CanvasRenderingContext2D): Unit = {
    g.beginPath()
    g.fillStyle = "black"
    g.fillRect(x - (width / 2), y - (length / 2), width, length)

  }
}

