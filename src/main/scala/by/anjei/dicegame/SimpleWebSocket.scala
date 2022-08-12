package by.anjei.dicegame

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.Flow

class SimpleWebSocket() extends Directives {
  private val defaultRoute: Route =
    get {
      pathSingleSlash {
        complete("Welcome to Dice Game")
      }
    }

  val flow: Flow[Message, Message, _] = Flow[Message].collect {
    case TextMessage.Strict(msg) => TextMessage.Strict(msg)
  }

  val gameRoute: Route =
    path("dice-game") {
      get {
        handleWebSocketMessages(flow)
      }
    }

  val route: Route = defaultRoute ~ gameRoute

}