package utils

import org.apache.commons.dbcp.BasicDataSource
import play.Play._
import slick.session.{Database => SlickDatabase}

/**
 *
 * @author Piotr Jędruszuk <p.jedruszuk@semantive.com>
 */
object Database {

  val pool = {
    val dataSource = new BasicDataSource
    dataSource.setUrl(conf("db.default.url"))
    dataSource.setUsername(conf("db.default.user"))
    dataSource.setPassword(conf("db.default.password"))
    dataSource.setDriverClassName(conf("db.default.driver"))
    dataSource
  }

  def createConnection() = SlickDatabase forDataSource pool

  private def conf(key: String) = application().configuration().getString(key)

}
