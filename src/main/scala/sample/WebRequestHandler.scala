package sample

import commando.web._
import sample.core.RootHandler

class WebRequestHandler(val application: Application) extends RequestHandler {

  lazy val rootHandler = new RootHandler(application.getConfig)

  lazy val routes = List[Route[RequestHandler]](
    new Route("home", Method.Any, "/", rootHandler),
    new Route("home", Method.Any, "/", rootHandler)
  )

  lazy val router = new Router(routes)

  override def handle(request: Request): Response = {
    router.getMatch(request) match {
      case null => new Response("Not found", 404)
      case matchedRoute => matchedRoute.value.handle(request)
    }
  }
}
