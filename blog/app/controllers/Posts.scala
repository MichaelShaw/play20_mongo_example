package controllers

import _root_.models.{Comment, Post, User}
import play.api._
import data.Form
import mvc._
import org.bson.types.ObjectId

import play.api.data._
import play.api.data.Forms._

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 29/07/12
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */



object Posts extends ApplicationController {
  def index = Authenticated { implicit request =>
    val posts = Post.findAll().map { p =>
      p.copy(author = User.dao.findOneById(p.userId))
    }
    Ok(views.html.posts.index(posts))
  }

  def show(id:ObjectId) = Authenticated { implicit request =>
    Post.findOneById(id).map( post=>
      Ok(views.html.posts.show(
        post.copy(
          author = User.dao.findOneById(post.userId),
          comments = post.comments.map { c =>
            c.copy(author = User.dao.findOneById(c.userId))
          }
        ),
        newCommentForm
      ))
    ).getOrElse(NotFound)
  }

  val newPostForm = Form(
    mapping(
      "title" -> text(minLength = 4),
      "content" -> text
    )((title, content) => Post(title = title, content = content))
      ((post) => Some(post.title, post.content))
  )

  def newPost = Authenticated { implicit request =>
    Ok(views.html.posts.newPost(newPostForm))
  }

  def create = Authenticated { implicit request =>
    newPostForm.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.posts.newPost(formWithErrors))
      },
      value => {
        val postWithUser = value.copy(userId = request.user.id)
        Post.save(postWithUser)
        Redirect(routes.Posts.index()).flashing("success" -> "Post created")
      }
    )
  }

  def edit(id:ObjectId) = Authenticated { implicit request =>
    Post.dao.findOneById(id).map { post =>
      Ok(views.html.posts.edit(post, newPostForm.fill(post)))
    }.getOrElse {
      NotFound("Couldnt find that post")
    }
  }

  def update(id:ObjectId) = Authenticated { implicit request =>
    Post.dao.findOneById(id).map { post =>
      newPostForm.bindFromRequest.fold(
        formWithErrors => Ok(views.html.posts.edit(post, formWithErrors)),
        value => {
          val updatedPost = post.copy(
            title = value.title,
            content = value.content
          )
          Post.save(updatedPost)
          Redirect(routes.Posts.index()).flashing("success" -> "Post updated")
        }
      )
    }.get
  }

  def destroy(id:ObjectId) = Authenticated { implicit request =>
    Post.dao.findOneById(id).map { post =>
      Post.remove(post)
      Redirect(routes.Posts.index()).flashing("success" -> "Post destroyed")
    }.getOrElse {
      Redirect(routes.Posts.index()).flashing("success" -> "Couldnt find post")
    }
  }

      //
}
