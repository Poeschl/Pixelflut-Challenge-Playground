package xyz.poeschl.challengeplayground

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import kotlin.concurrent.thread

fun main(args: Array<String>): Unit = mainBody {
    ArgParser(args).parseInto(::Args).run {
        println("Start drawing on $host:$port")
        val drawer = PlaygroundDrawer(host, port)
        println("Awaiting commands on $host:$controlPort")
        val restInterface =
            RestInterface(
                controlPort,
                drawer
            )
        val cliInterface = CliInterface(drawer, restInterface)

        thread {
            drawer.start()
        }
        thread {
            restInterface.start()
        }
        thread {
            if (enableCLI) {
                cliInterface.start()
            }
        }
    }
}

class Args(parser: ArgParser) {
    val host by parser.storing("--host", help = "The host of the pixelflut server").default("localhost")
    val port by parser.storing("-p", "--port", help = "The port of the pixelflut server") { toInt() }.default(1234)
    val enableCLI by parser.flagging("--enableCli", help = "Enables the command-line cli").default(false)
    val controlPort by parser.storing("-c", "--controlport", help = "The port for commands") { toInt() }.default(4321)
}
