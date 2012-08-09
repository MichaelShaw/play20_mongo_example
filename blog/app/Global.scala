/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 29/07/12
 * Time: 10:29 PM
 * To change this template use File | Settings | File Templates.
 */

import _root_.models.{Post, User}
import com.mongodb.casbah.Imports._
import play.api._
import models._
import se.radley.plugin.salat._


object Global extends GlobalSettings {
  override def onStart(app: Application) {
    if (User.count() == 0) {
      println("removing all users")
      User.removeByIds(User.findAll().map(u => u.id).toList)

      println("saving the user steve")
      val steve = User(username = "steve", password = "please")
      User.save(steve)

      val postTitles = Seq("New Apple Product", "Microsoft makes first loss")

      Post.removeByIds(Post.findAll().map(p => p.id).toList)
      postTitles.foreach { pt =>
        Post.save(
          Post(title = pt, content = "content for %s".format(pt), userId = steve.id)
        )
      }
    }
  }
}
