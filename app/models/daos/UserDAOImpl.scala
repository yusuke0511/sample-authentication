package models.daos

import java.util.UUID

import javax.inject._
import com.mohiva.play.silhouette.api.LoginInfo
import models.User

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json._

import scala.concurrent.Future
import play.modules.reactivemongo._
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.collection.mutable

/**
 * Give access to the user object.
 */
class UserDAOImpl @Inject() (val reactiveMongoApi: ReactiveMongoApi) extends UserDAO {

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("silhouette.user"))

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  @deprecated("Use `find` with optional `projection`", "0.16.0")
  def find(loginInfo: LoginInfo): Future[Option[User]] = {
    val query = Json.obj("loginInfo" -> loginInfo)
    collection.flatMap(_.find(query).one[User])
  }

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  @deprecated("Use `find` with optional `projection`", "0.16.0")
  def find(userID: UUID) = {
    val query = Json.obj("userID" -> userID)
    collection.flatMap(_.find(query).one[User])
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User] = {
    collection.flatMap(_.update(Json.obj("userID" -> user.userID), user, upsert = true))
    Future.successful(user)
  }
}
