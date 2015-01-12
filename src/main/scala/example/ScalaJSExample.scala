package example

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import scala.util.Random

case class Point(x: Int, y: Int) {
  def +(p: Point) = Point(x + p.x, y + p.y)

  def /(d: Int) = Point(x / d, y / d)
}

@JSExport
object ScalaJSExample {
  @JSExport
  def main(canvas: dom.HTMLCanvasElement): Unit = {

    // setup
    val renderer = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    canvas.width = 500
    canvas.height = 500

    renderer.fillStyle = "#f8f8f8"

    renderer.fillRect(0, 0, canvas.width, canvas.height)

    // code
    renderer.fillStyle = "black"

    var down = false

    // try another route
    canvas.addEventListener("keypress",
      (e: dom.Event) => {
        renderer.fillRect(250, 250, 100, 100)
      }, true)

    // WTF?
    canvas.oninput =
      (e: Any) => {
        val rect = canvas.getBoundingClientRect()
        renderer.fillRect(0, 0, canvas.width, canvas.height)
      }


    canvas.onmousedown =
      (e: dom.MouseEvent) => {
        down = true
      }

    canvas.ondblclick =
      (e: dom.MouseEvent) => {
        renderer.strokeText("double click", canvas.width / 2, canvas.height / 2)
      }


    canvas.onclick =
      (e: dom.MouseEvent) => {
        renderer.strokeRect(canvas.width / 2, canvas.height / 2, 40 + Random.nextInt(40), 40 + Random.nextInt(40))
      }

    canvas.onmouseup =
      (e: dom.MouseEvent) => {
        down = false
      }

    canvas.onmousemove = {
      (e: dom.MouseEvent) => {
        val rect = canvas.getBoundingClientRect()
        renderer.lineWidth = Random.nextDouble()
        if (down) renderer.strokeRect(
          e.clientX - rect.left,
          e.clientY - rect.top,
          Random.nextInt(10), Random.nextInt(10)
        )
      }

    }

  }
}
