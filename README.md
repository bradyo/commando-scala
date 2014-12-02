commando-scala
==============

Experimental Scala web framework based on composition of `RequestHandler` objects.

```scala
class PostUserHandler(
  val userPostValidator: UserPostValidator, 
  val userService: UserService
) extends AuthenticatedRequestHandler {
    
    def handle(request: AuthenticatedRequest): Response = {
      val authorized = request.getAccessToken.hasRole(Roles.Admin);
      if (authorized) {
        val post = new UserPost(request.getParams())
        val errors = userPostValidator.validate(post)
        if (errors.empty) {
          val newUser = userService.registerUser(post)
          return new UserResponse(newUser, 201)
        }
        else new ValidationErrorResponse("Invalid request", errors)
      } 
      else NotAllowedResponse("Not allowed to post Users");
    }
}
```

```scala
class WebRequestHandler(val app: Application) extends RequestHandler {

  lazy val homeHandler = new HomeHandler(app.getConfig)
  lazy val userModuleHandler = app.getUserModuleHandler
  lazy val noteModuleHandler = app.getNoteModuleHandler
  
  lazy val router = new Router[Route[RequestHandler]](
    new Route("home", Method.Any, "/", homeHandler),
    new PathRoute("user-module", "/users", userModuleHandler),
    new PathRoute("note-module", "/notes", noteModuleHandler)
  ))

  override def handle(request: Request): Response = {
    router.getMatch(request) match {
      case null => new Response("Not found", 404)
      case matchedRoute => matchedRoute.value.handle(request)
    }
  }
}
```
