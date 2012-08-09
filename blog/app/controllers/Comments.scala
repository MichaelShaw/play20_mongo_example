package controllers

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import models.{Post, Comment}
import org.bson.types.ObjectId

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 7/08/12
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */

object Comments extends ApplicationController {
  def create(postId:ObjectId) = Authenticated { implicit request =>
    Post.dao.findOneById(postId).map { post =>
      newCommentForm.bindFromRequest.fold(
        formWithErrors => {
          Ok(views.html.posts.show(post, formWithErrors))
        },
        value => {
          val updatedPost = post.copy(
            comments = post.comments :+ value.copy(
              userId = request.user.id
            )
          )

          Post.save(updatedPost)

          Redirect(routes.Posts.show(post.id)).flashing("success" -> "Comment created")
        }
      )
    }.get
  }
}
