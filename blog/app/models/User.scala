package models

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 28/07/12
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */

import play.api.Play.current
import java.util.Date
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._

import com.novus.salat.dao._
import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import com.novus.salat.{TypeHintFrequency, StringTypeHintStrategy, Context}
import play.api.Play
import play.api.Play.current

import MongoContext.context

case class User(id: ObjectId = new ObjectId,
                 username: String,
                 password: String,
                 added: Date = new Date(),
                 updated: Option[Date] = None,
                 deleted: Option[Date] = None,
                 @Key("company_id")company: Option[ObjectId] = None
                 )

object User extends ModelCompanion[User, ObjectId] {
  val collection = mongoCollection("users")
  val dao = new SalatDAO[User, ObjectId](collection = collection) {}

  def findWithUsernameAndPassword(username:String, password:String) :Option[User] = {
    dao.findOne(MongoDBObject("username" -> username, "password" -> password))
  }
  def findOneByUsername(username: String): Option[User] = dao.findOne(MongoDBObject("username" -> username))
  def findByCountry(country: String) = dao.find(MongoDBObject("address.country" -> country))
}