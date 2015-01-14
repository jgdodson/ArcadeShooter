package ArcadeShooter

import org.scalajs.dom


// TODO: List for all spawners, then pattern match to determine where the spawn will go.

// TODO: The next generation consists ONLY of spawn (wouldn't need vars!)
//  Variables result from mangling time. To still use variables, one could
//  enforce epochs that allow at most one update at a time.

// TODO: Create a background class and add an instance to World
//  It will have an update method. More, to the point, Citizens will be able to
//  affect the world (leave a trail of dust, etc.). This will allow a fire and forget
//  approach to lingering effects. Each item that is left behind will be some object that has
//  a render and update method that the background will call (or these could just be
//  added directly to the world if we desired these remnants to interact with other citizens.)

class World(worldCanvas: dom.HTMLCanvasElement, worldWidth: Int, worldHeight: Int) {

  // Setup the Canvas
  worldCanvas.tabIndex = 0
  worldCanvas.style.outline = "none"
  worldCanvas.focus()
  worldCanvas.width = worldWidth
  worldCanvas.height = worldHeight

  // Setup key-controls for the canvas.
  worldCanvas.onkeydown = (e: dom.KeyboardEvent) => e.keyCode match {
    // moving left/right
    case 37 => shooters.map(s => s.startMovingLeft())
    case 39 => shooters.map(s => s.startMovingRight())

    // shooting commands
    case 87 => shooters.map(s => s.fireSingle())
    case 69 => shooters.map(s => s.fireTriple())
    case 81 => shooters.map(s => s.shockWave())
  }

  worldCanvas.onkeyup = (e: dom.KeyboardEvent) => e.keyCode match {
    // moving left/right
    case 37 => shooters.map(s => s.stopMovingLeft())
    case 39 => shooters.map(s => s.stopMovingRight())
  }


  private val worldCTX = worldCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  private var generators: List[Generator] = Nil
  private var shooters: List[Shooter] = Nil
  private var bullets: List[Bullet] = Nil
  private var enemies: List[Enemy] = Nil

  // Introduces a new Enemy to the world.
  def addEnemy(r: Int): Unit = {
    enemies = new Enemy(r, worldWidth, worldHeight) :: enemies
  }

  // Introduces a new Shooter to the world.
  def addShooter(): Unit = {
    shooters = new Shooter(worldWidth, worldHeight) :: shooters
  }

  def addGenerator(freq: Int): Unit = {
    generators = new Generator(freq, worldWidth, worldHeight) :: generators
  }


  // Allows a single bullet to kill multiple enemies and
  // (in future?) multiple bullets to kill a single enemy.
  private def CheckCollisions(bs: List[Bullet], es: List[Enemy]): (List[Bullet], List[Enemy]) = {

    // Checks one bullet against a list of enemies.
    def loop1(b: Bullet, es: List[Enemy]): (List[Bullet], List[Enemy]) = {
      val (hits, misses) = es partition { enemy => b isNear enemy}
      if (hits.isEmpty) (List(b), misses)
      else (Nil, misses)
    }

    // Calls loop1 for all bullets.
    def loop2(bs: List[Bullet], e: List[Enemy], remainingBul: List[Bullet]): (List[Bullet], List[Enemy]) = bs match {
      case fb :: rest =>
        val (b, es) = loop1(fb, e)
        loop2(rest, es, b ::: remainingBul)
      case Nil => (remainingBul, e)
    }

    // Start the calculation
    loop2(bs, es, Nil)
  }

  private def render(): Unit = {

    worldCTX.clearRect(0, 0, worldWidth, worldHeight)

    // Render the enemies
    for (e <- enemies) e.render(worldCTX)

    // Render the bullets.
    for (b <- bullets) b.render(worldCTX)

    // Render the shooters last (always on top).
    for (s <- shooters) s.render(worldCTX)

    // Display the number of shots fired


    worldCTX.font = "30px calibri"
    worldCTX.fillStyle = "black"
    worldCTX.fillText("Fired: " + shooters.headOption.map(s => s.numShotsFired).getOrElse(0).toString(),10,30)
  }

  private def update(): Unit = {

    // Firstly, handle the collisions.
    // If handled last, after the updates, the collisions would
    // appear to happen a frame before they actually do.
    val p = CheckCollisions(bullets, enemies)
    bullets = p._1
    enemies = p._2

    // Update the bullets
    for (b <- bullets) b.update()
    bullets = bullets.filter(x => !x.expired())

    // Update the enemies.
    for (e <- enemies) e.update()

    // Update the shooters.
    for (s <- shooters) {
      s.update()
      bullets = s.spawn() ::: bullets
    }

    for (g <- generators) enemies = g.spawn() ::: enemies

  }

  def bigBang(t: Int): Unit = {

    def loop(): Unit = {
      render()
      update()
    }

    // Eta-expansion of method to function
    dom.setInterval(loop _, t)
  }


}
