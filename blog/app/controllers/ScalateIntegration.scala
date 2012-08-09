package controllers

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 28/07/12
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */


import play.api._
import http.{Writeable, ContentTypeOf, ContentTypes}
import mvc.Codec
import play.api.Play.current
import org.fusesource.scalate.layout.DefaultLayoutStrategy

object Scalate {
  import org.fusesource.scalate._
  import org.fusesource.scalate.util._

//  var format = Play.configuration.getString("scalate.format") match {
//    case Some(configuredFormat) => configuredFormat
//    case _ => "test"
//  }

  var format = Some("jade")

  lazy val scalateEngine = {
    val engine = new TemplateEngine
    engine.resourceLoader = new FileResourceLoader(Some(Play.getFile("/app/views")))
    engine.layoutStrategy = new DefaultLayoutStrategy(engine, Play.getFile("/app/views/layouts/default." + format).getAbsolutePath)
    engine.classpath = Play.getFile("/tmp/classes").getAbsolutePath
    engine.workingDirectory = Play.getFile("tmp")
    engine.combinedClassPath = true
    engine.classLoader = Play.classloader
    engine
  }

  def apply(template: String) = {
    Template(template)
  }

  case class Template(name: String) {
    def render(args: (Symbol, Any)*) = {
      println("rendering template -> " + name)
      println("args -> " + args)
      ScalateContent{
        scalateEngine.layout(name, args.map {
          case (k, v) => k.name -> v
        } toMap)
      }
    }
  }

  case class ScalateContent(val cont: String)

  implicit def writeableOf_ScalateContent(implicit codec: Codec): Writeable[ScalateContent] = {
    Writeable[ScalateContent](scalate => codec.encode(scalate.cont))
  }

  implicit def contentTypeOf_ScalateContent(implicit codec: Codec): ContentTypeOf[ScalateContent] = {
    ContentTypeOf[ScalateContent](Some(ContentTypes.HTML))
  }

}
