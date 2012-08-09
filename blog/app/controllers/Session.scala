package controllers

import play.api.mvc.{Action, Controller}
import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import models.{User, Post}

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 7/08/12
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */

case class UserSession(username: String, password:String)

object Session extends Controller {
  val loginForm = Form(
    mapping(
      "username" -> text(minLength = 4),
      "password" -> text(minLength = 4)
    ) (UserSession.apply) (UserSession.unapply).verifying(
      "Invalid username or password",
      u => User.findWithUsernameAndPassword(u.username, u.password).isDefined
    )
  )

  def newSession = Action { implicit request =>
    Ok(views.html.session.newSession(loginForm))
  }

  def create = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.session.newSession(formWithErrors))
      },
      value => {
        Redirect(routes.Posts.index()).withSession(
          session + ("username" -> value.username)
        ).flashing(
          "success" -> "You're now logged in as %s".format(value.username)
        )
      }
    )
  }

  def destroy = Action { implicit request =>
    Redirect(routes.Session.newSession()).withSession(
      session - "username"
    ).flashing(
      "success" -> "You're logged out"
    )
  }
}
