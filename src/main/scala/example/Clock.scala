package example

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import scala.scalajs.js
import scala.util.Random

@JSExport
object Clock {

  @JSExport
  def main(canvas: dom.HTMLCanvasElement): Unit = {

    val renderer = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    canvas.width = 500
    canvas.height = 500

    renderer.textAlign = "center"
    renderer.textBaseline = "middle"

    renderer.fillStyle = "black"


    var xpos = canvas.width / 2.0
    var ypos = canvas.height / 2.0
    var alpha = 0.5


    def render = {
      val clock = new js.Date()
      renderer.clearRect(0, 0, canvas.width, canvas.height)

      renderer.font = "75px sans-serif"
      alpha = alpha + (0.1 * math.pow(-1.0, Random.nextInt(2)))
      renderer.globalAlpha = alpha

      val h = clock.getHours()
      val m = clock.getMinutes()
      val s = clock.getSeconds()


      xpos = xpos + (math.pow(-1, Random.nextInt(2)) * (3.0 * Random.nextDouble))
      ypos = ypos + (math.pow(-1, Random.nextInt(2)) * (3.0 * Random.nextDouble))

      renderer.fillText(
        Seq(h, m, s).mkString(":"),
        xpos,
        ypos
      )

    }

    // eta-expansion is necessary here
    dom.setInterval(render _, 20)

  }
}
