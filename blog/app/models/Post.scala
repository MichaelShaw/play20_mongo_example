package models

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 28/07/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */


import se.radley.plugin.salat._

import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.novus.salat.{TypeHintFrequency, StringTypeHintStrategy, Context}
import play.api.Play.current


import models.MongoContext.context

case class Post(id: ObjectId = new ObjectId,
                userId: ObjectId = new ObjectId,
                title:String = "",
                content:String = "",
                tags: Seq[String] = Seq.empty,
                comments: Seq[Comment] = Seq.empty,
                @Ignore author: Option[User] = None)

case class Comment(userId: ObjectId = new ObjectId,
                   content:String = "",
                   @Ignore author: Option[User] = None)

object Post extends ModelCompanion[Post, ObjectId] {
  val collection = mongoCollection("posts")
  val dao = new SalatDAO[Post, ObjectId](collection = collection) {}
}