package ArcadeShooter.Examples.Scalaga

import ArcadeShooter.Library.Shooter
import org.scalajs.dom

// For arcade-style games. (i.e. Galaga)
// A walker is a user-controlled agent that walks across the bottom of the canvas.
class Spaceship(_pace: Int, worldWidth: Int, worldHeight: Int) extends Shooter(worldWidth, worldHeight) {

  val halfWidth = 22
  val halfHeight = 24

  var numShotsFired = 0

  // Pace of the walker, that is, the velocity when walking left or right.
  val pace: Int = math.abs(_pace)

  // Position
  var x: Int = worldWidth / 2
  var y: Int = worldHeight - halfHeight - 20

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

  // 0 to 31
  def render(g: dom.CanvasRenderingContext2D): Unit = {

    // Setup
    val squareSize = 3
    var currX = x - halfWidth
    var currY = y - halfHeight

    val pattern =
      "nnnnnnnwnnnnnnne" +
        "nnnnnnnwnnnnnnne" +
        "nnnnnnnwnnnnnnne" +
        "nnnnnnwwwnnnnnne" +
        "nnnnnnwwwnnnnnne" +
        "nnnnnnwwwnnnnnne" +
        "nnnrnnwwwnnrnnne" +
        "nnnrnwwwwwnrnnne" +
        "rnnwbwwrwwbwnnre" +
        "rnnbwwrrrwwbnnre" +
        "wnnwwwrwrwwwnnwe" +
        "wnwwwwwwwwwwwnwe" +
        "wwwwwrwwwrwwwwwe" +
        "wwwnrrwwwrrnwwwe" +
        "wwnnrrnwnrrnnwwe" +
        "wnnnnnnwnnnnnnwe"



    def handleSquare(c: Char): Unit = c match {
      case 'e' =>
        currX -= 15 * squareSize
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

