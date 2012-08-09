package controllers

import play.api.mvc._
import models.{Comment, User}
import play.api.data.Form
import play.api.data.Forms._
import scala.Some


/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 7/08/12
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */

case class AuthenticatedRequest(
user: User, request: Request[AnyContent]
) extends WrappedRequest(request)

class ApplicationController extends Controller {
  def Authenticated(f:AuthenticatedRequest => Result) : Action[AnyContent] = {
    Action { request =>
      request.session.get("username").flatMap { username =>
        User.findOneByUsername(username).map { user =>
          f(AuthenticatedRequest(user, request))
        }
      }.getOrElse {
        Redirect(routes.Session.newSession()).flashing("success" -> "You need to login.")
      }
    }
  }

  val newCommentForm = Form(
    mapping(
      "content" -> text
    )((content) => Comment(content = content))
      ((comment) => Some(comment.content))
  )
}
