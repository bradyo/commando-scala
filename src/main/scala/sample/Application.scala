package sample

class Application extends commando.Application {

  setWebRequestHandler(new WebRequestHandler(this))

  def getConfig: Map[String, String] = Map(
    "one" -> "value 1",
    "two" -> "value 2"
  )

}
