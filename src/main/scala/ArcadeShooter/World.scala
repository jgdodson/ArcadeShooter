package ArcadeShooter

import org.scalajs.dom


// List for all spawners, then pattern match to determine where the spawn will go.

// TODO: The next generation consists ONLY of spawn (wouldn't need vars!)
//  Variables result from mangling time. To still use variables, one could
//  enforce epochs that allow at most one update at a time.

class World() {

  var shooters: List[Shooter] = Nil
  var bullets: List[Bullet] = Nil
  var enemies: List[Enemy] = Nil

  // Introduces a new Enemy to the world.
  def addEnemy(e: Enemy): Unit = {
    enemies = e :: enemies
  }

  // Introduces a new Shooter to the world.
  def addShooter(s: Shooter): Unit = {
    shooters = s :: shooters
  }


  // Allows a single bullet to kill multiple enemies and
  // (in future?) multiple bullets to kill a single enemy.
  def CheckCollisions(bs: List[Bullet], es: List[Enemy]): (List[Bullet], List[Enemy]) = {

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

    loop2(bs, es, Nil)
  }

  def render(g: dom.CanvasRenderingContext2D): Unit = {

    // Render the shooters.
    for (s <- shooters) s.render(g)

    // Render the bullets.
    for (b <- bullets) b.render(g)

    // Render the enemies
    for (e <- enemies) e.render(g)
  }

  def update(): Unit = {

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

    // Finally, handle the collisions.
    val p = CheckCollisions(bullets, enemies)
    bullets = p._1
    enemies = p._2

  }

}
