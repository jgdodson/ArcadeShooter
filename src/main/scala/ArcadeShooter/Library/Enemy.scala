package ArcadeShooter.Library

abstract class Enemy(worldWidth: Int, worldHeight: Int) extends Physical(worldWidth, worldHeight) {

  def expired(): Boolean = {
    x < 0 || x > worldWidth || y < 0 || y > worldHeight
  }

}
