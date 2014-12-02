package commando

package object web {

  object Method extends Enumeration {
    type Method = Value
    val Any, Get, Post, Put, Delete = Value
  }
  import Method._

  class Request()

  class MatchedRequest(matchedRoute: MatchedRoute) extends Request

  class Response(
    val content: String,
    val status: Int = 200,
    val headers: Map[String, String] = Map.empty
  )

  class JsonResponse(
    override val content: String,
    override val status: Int = 200,
    override val headers: Map[String, String] = Map.empty
  ) extends Response(
    content,
    status,
    headers + ("Content-type" -> "application/json")
  )

  abstract class RequestHandler {
    def handle(request: Request): Response
  }

  class DefaultRequestHandler extends RequestHandler {
    def handle(request: Request) = new Response("Commando Application", 200)
  }

  abstract class WebExceptionHandler {
    def handle(request: Request, exception: Exception)
  }

  class DefaultWebExceptionHandler extends WebExceptionHandler {
    def handle(request: Request, exception: Exception) = {
      val content = "Application error: " + exception.getMessage +
        "\n\n" + exception.getStackTrace.toString
      new Response(content, 500)
    }
  }

  class Route[E](
    val name: String,
    val method: Method,
    val path: String,
    val value: E
  )

  class PathRoute[E](
    name: String,
    path: String,
    value: E
  ) extends Route(
    name,
    Method.Any,
    path,
    value
  )

  class MatchedRoute[E](
    override val name: String,
    override val method: Method,
    override val path: String,
    override val value: E,
    val params: Map[String, Object]
  ) extends Route(
    name,
    method,
    path,
    value
  )

  class Router[E](val routes: List[Route[E]]) {
    def getMatch(request: Request): MatchedRoute[E] = null // todo
  }
}
