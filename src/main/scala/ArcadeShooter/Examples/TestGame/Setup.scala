package ArcadeShooter.Examples.TestGame

import ArcadeShooter.Library.World
import org.scalajs.dom

object Setup {
   def start(canvas: dom.HTMLCanvasElement, worldWidth: Int, worldHeight: Int, t: Int): Unit = {

     val world: World = new World(canvas, worldWidth, worldHeight)
     world.addShooter(new Walker(8, 25, 15, worldWidth, worldHeight))
     world.addGenerator(new SampleGenerator(30, worldWidth, worldHeight))

     world.bigBang(t)

   }
 }
