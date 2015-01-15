package ArcadeShooter.Examples.SimpleGame

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

// DONE: Refactor into multiple files
// DONE: Add method to World class that introduces citizens into world
// DONE: Add collision detection. (keep separate list of bullets and enemies?)
// TODO: Tweak collision detection.
// TODO: Add infobars (ammo, lives, etc.)

@JSExport
object SimpleApp {

  @JSExport
  def main(canvas: dom.HTMLCanvasElement): Unit = {

    Setup.start(canvas, 1000, 600, 20)

  }

}
