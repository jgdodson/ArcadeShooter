package ArcadeShooter.Examples.TestGame

import ArcadeShooter.Library.Generator
import org.scalajs.dom

import scala.util.Random

// DONE: Should a Generator be a citizen?
//  Time to expand the class hierarchy, I think.
class SampleGenerator(freq: Int, worldWidth: Int, worldHeight: Int) extends Generator {

  def expired(): Boolean = false

  def update(): Unit = ()

  def render(g: dom.CanvasRenderingContext2D): Unit = ()

  override def spawn(): List[SampleEnemy] = {
    if (Random.nextInt(freq) == 0) List(new SampleEnemy(10, worldWidth, worldHeight))
    else Nil
  }

}
