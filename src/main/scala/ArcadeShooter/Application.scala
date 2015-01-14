package ArcadeShooter

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

// DONE: Refactor into multiple files
// DONE: Add method to World class that introduces citizens into world
// DONE: Add collision detection. (keep separate list of bullets and enemies?)
// TODO: Tweak collision detection.
// TODO: Add infobars (ammo, lives, etc.)


@JSExport
object Application {

  @JSExport
  def main(canvas: dom.HTMLCanvasElement): Unit = {

    //dom.alert("Single Shot: W\n" + "TripleShot: E\n" + "Shockwave: Q")

    // DONE: provide the world class with methods for adding citizens. This
    // will make it easier to inform the citizens of certain world-properties.

    val world: World = new World(canvas, 500, 500)
    world.addShooter()

    world.bigBang()


  }

}
