package by.anjei.dicegame

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val context: ExecutionContextExecutor = system.dispatcher

    val host = "localhost"
    val port = 80
    val webSocket = new SimpleWebSocket()

    val binding = Http().newServerAt(host, port).bind(webSocket.route)
    binding.onComplete {
      case Success(binding) =>
        val runningAddress = binding.localAddress
        println(s"Server started at ${runningAddress.getHostName}:${runningAddress.getPort}")
      case Failure(ex) =>
        println(s"Binding failed. Reason: ${ex.getMessage}")
        system.terminate()
    }

    sys.addShutdownHook {
      binding.flatMap(_.terminate(hardDeadline = 3.seconds)).flatMap { _ =>
        system.terminate()
      }
    }
  }
}
