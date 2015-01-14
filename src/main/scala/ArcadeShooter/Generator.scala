package ArcadeShooter

import org.scalajs.dom
import scala.util.Random

// TODO: Should a Generator be a citizen?
//  Time to expand the class hierarchy, I think.
class Generator(freq: Int, worldWidth: Int, worldHeight: Int) extends Citizen {

  val halfLength = 0
  val halfWidth = 0

  var x = 0
  var y = 0

  val vx = 0
  val vy = 0

  def render(g: dom.CanvasRenderingContext2D): Unit = ()

  override def spawn(): List[Enemy] = {
    if (Random.nextInt(freq) == 0) List(new Enemy(10, worldWidth, worldHeight))
    else Nil
  }

}
