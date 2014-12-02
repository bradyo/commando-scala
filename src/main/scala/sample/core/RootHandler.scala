package sample.core

import commando.web.{Response, Request, RequestHandler}

class RootHandler(config: Map[String, String]) extends RequestHandler {
  override def handle(request: Request): Response = {
    new Response(config.toString())
  }
}
