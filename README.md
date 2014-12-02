commando-scala
==============

Experimental Scala web framework based on composition of `RequestHandler` objects.

```scala
abstract class AuthenticatedRequestHandler {
  def handle(request: AuthenticatedRequest): Response
}
class PostUserHandler(
  val userPostValidator: UserPostValidator, 
  val userService: UserService
) extends AuthenticatedRequestHandler {
    
    def handle(request: AuthenticatedRequest): Response = {
      if (! request.getAccessToken.hasRole(Roles.Admin)) 
        return new NotAllowedResponse("Not allowed to post Users")

      val post = new UserPost(request.getParams())
      val errors = userPostValidator.validate(post)
      if (! errors.empty) 
        return new ValidationErrorResponse("Invalid request", errors)
      
      val newUser = userService.registerUser(post)
      
      new UserResponse(newUser, 201)
    }
}
```

```scala
class WebRequestHandler(val app: Application) extends RequestHandler {

  lazy val guard = app.getGuard
  lazy val homeHandler = new HomeHandler(app.getConfig)
  lazy val userModuleHandler = app.getUserModuleHandler
  lazy val noteModuleHandler = app.getNoteModuleHandler
  
  lazy val router = new Router[Route[RequestHandler]](
    new Route("home", HttpMethod.Get, "/", homeHandler),
    new PathRoute("user-module", "/users", userModuleHandler),
    new PathRoute("note-module", "/notes", noteModuleHandler)
  ))

  override def handle(request: Request): Response = {
    router.getMatch(request) match {
      case null => new Response("Not found", 404)
      case matchedRoute => {
        val authenticatedRequest = guard.authenticate(request)
        matchedRoute.value.handle(authenticatedRequest)
      }
    }
  }
}
```
