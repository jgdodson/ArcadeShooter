package ArcadeShooter

import scala.util.Random

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

// DONE: Refactor into multiple files
// DONE: Add method to World class that introduces citizens into world
// DONE: Add collision detection. (keep separate list of bullets and enemies?)
// TODO: Tweak collision detection.
// TODO: Add infobars (ammo, lives, etc.)

/**
 * Created by JordanDodson on 1/13/15.
 */
@JSExport
object Application {

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

    val world: World = new World()
    world.addShooter(wakka)
    world.addEnemy(new Enemy())

    def renderWorld(): Unit = {
      renderer.clearRect(0, 0, size, size)

      if (Random.nextInt(30) == 1) world.addEnemy(new Enemy())
      world.render(renderer)
      world.update()
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
