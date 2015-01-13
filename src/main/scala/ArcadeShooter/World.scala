package ArcadeShooter

import org.scalajs.dom

/**
 * Created by JordanDodson on 1/13/15.
 */
class World() {


  // (shooters, bullets, enemies)
  var shooters: List[Shooter] = Nil
  var bullets: List[Bullet] = Nil
  var enemies: List[Enemy] = Nil

  def addEnemy(e: Enemy): Unit = {
    enemies = e :: enemies
  }

  def addShooter(s: Shooter): Unit = {
    shooters = s :: shooters
  }


  // Checks for Bullet/Enemy collisions and annihilates accordingly.
  def CheckCollisions(b: List[Bullet], e: List[Enemy]): (List[Bullet], List[Enemy]) = {

    def loop1(b: Bullet, rem: List[Enemy], acc: List[Enemy], hit: Boolean): (List[Bullet], List[Enemy]) = {
      if (hit) (Nil, rem ::: acc)
      else rem match {
        case fe :: rest =>
          if (b.isNear(fe.x, fe.y, fe.r)) loop1(b, rest, acc, true)
          else loop1(b, rest, fe :: acc, false)

        case Nil => (List(b), acc)
      }
    }

    def loop2(bs: List[Bullet], e: List[Enemy], ab: List[Bullet]): (List[Bullet], List[Enemy]) = {
      bs match {
        case fb :: rest =>
          val (b, es) = loop1(fb, e, Nil, false)
          loop2(rest, es, b ::: ab)

        case Nil => (ab, e)
      }
    }

    // Result
    loop2(b, e, Nil)
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
