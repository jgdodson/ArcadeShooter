package ArcadeShooter


// Might not need a class for this anymore, since all citizens have a spawn method.
// Instead, have an object that contains a collection of different style shooters.
class Shooter extends Walker(8, 25, 15) {

  // A flag indicating what the Shooter should spawn(fire) during the next round.
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


  override def spawn(): List[Bullet] = {

    // We will match on the current state of the nextShot flag.
    val toShoot = nextShot

    // Always reset for the next update.
    nextShot = 0

    toShoot match {
      case 1 => List(new Bullet(x, y - halfLength, 0, -10, 5))

      case 2 => for (i <- List.range(-2, 4, 2)) yield new Bullet(x, y - halfLength, i, -10, 5)

      case 3 => for (i <- List.range(0, 21)) yield new Bullet(x, y + halfLength, (250 * math.cos((i / 20.0) * math.Pi)).toInt / 15, -(250 * math.sin((i / 20.0) * math.Pi)).toInt / 15, 5)

      case _ => Nil
    }
  }

}

