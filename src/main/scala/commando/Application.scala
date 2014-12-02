package commando

import commando.web._

class Application {
  var webRequestHandler: RequestHandler = new DefaultRequestHandler
  var webExceptionHandler: WebExceptionHandler = new DefaultWebExceptionHandler

  def setWebRequestHandler(handler: RequestHandler) = {
    webRequestHandler = handler
  }
  def setWebExceptionHandler(handler: WebExceptionHandler) = {
    webExceptionHandler = handler
  }
  def handleRequest(request: Request): Response = webRequestHandler.handle(request)
}

object Application {
  def main(args: Array[String]) = {
    println("Hi")
  }
}