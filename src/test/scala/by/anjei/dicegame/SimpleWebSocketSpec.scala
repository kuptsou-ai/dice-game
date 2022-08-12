package by.anjei.dicegame

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{ScalatestRouteTest, WSProbe}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SimpleWebSocketSpec extends AnyFunSuite with Matchers with ScalatestRouteTest {

  override def afterAll(): Unit = cleanUp()

  test("Webserver responds to default path") {
    val server = new SimpleWebSocket()
    Get() ~> server.route ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  test("Websocket responds with echo") {
    val server = new SimpleWebSocket()
    val wsClient = WSProbe()
    WS("/dice-game", wsClient.flow) ~> server.route ~>
      check {
        wsClient.sendMessage("Hello")
        wsClient.expectMessage("Hello")
        wsClient.sendCompletion()
        wsClient.expectCompletion()
      }
  }

}
