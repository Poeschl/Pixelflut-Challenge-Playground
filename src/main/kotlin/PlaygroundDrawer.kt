package io.github.poeschl.pixelflutchallenge.borders

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import io.github.poeschl.kixelflut.Painter
import io.github.poeschl.kixelflut.Pixelflut
import io.github.poeschl.kixelflut.Point
import io.github.poeschl.kixelflut.createRectPixels
import java.awt.Color
import kotlin.concurrent.thread
import kotlin.math.floor

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::Args).run {
        println("Start drawing on $host:$port")
        PlaygroundDrawer(host, port).start()
    }
}

class PlaygroundDrawer(host: String, port: Int) : Painter() {
    companion object {
        private const val SPLIT_COUNT = 3
        private val FINAL_CHALLENGE_POSITION = floor(SPLIT_COUNT / 2.0).toInt()
        private val FINAL_CHALLENGE_COLOR = Color.GRAY
    }

    private val pixelFlutInterface = Pixelflut(host, port)
    private val displaySize = pixelFlutInterface.getPlaygroundSize()
    private val playboxes = mutableListOf<Playbox>()

    private var inputRunning = true;

    override fun init() {
        println("Detected size $displaySize")

        val xSlice = displaySize.first / SPLIT_COUNT
        val ySlice = displaySize.second / SPLIT_COUNT

        for (y: Int in 0 until SPLIT_COUNT) {
            for (x: Int in 0 until SPLIT_COUNT) {
                if (x == FINAL_CHALLENGE_POSITION && y == FINAL_CHALLENGE_POSITION) {
                    val finalChallengeArea =
                        createRectPixels(Point(xSlice * x, ySlice * y), Pair(xSlice, ySlice), FINAL_CHALLENGE_COLOR)
                    pixelFlutInterface.paintPixelSet(finalChallengeArea)
                } else {
                    playboxes.add(Playbox(Point(xSlice * x, ySlice * y), Pair(xSlice, ySlice)))
                }
            }
        }
        println("Setup ${playboxes.size} playboxes")

        println("\nCommands: ")
        displayHelp()
        println()

        thread {
            var input: String
            do {
                print("> ")
                input = readLine() ?: ""
                handleInput(input)
            } while (inputRunning)
        }
    }

    override fun render() {
        playboxes.parallelStream().forEach { it.draw(pixelFlutInterface) }
    }

    override fun afterStop() {
        pixelFlutInterface.close()
    }

    private fun displayHelp() {
        println("blank -> Wipes the whole screen")
        println("blank <id> -> Wipes one playground (left to right starting at 0 from top left)")
        println("blank <id> <sector> -> Wipes one playground sector (sector are counted clockwise, starting top left)")
    }

    private fun handleInput(input: String) {
        val splitedInput = input.split(' ')
        when {
            input == "quit" -> {
                inputRunning = false
                stop()
            }
            input == "dummy" -> {
                pixelFlutInterface.paintPixelSet(
                    createRectPixels(
                        Point(0, 0),
                        pixelFlutInterface.getPlaygroundSize(),
                        Color.CYAN
                    )
                )
            }
            input == "blank" -> blankAll()
            input.startsWith("blank") && splitedInput.size == 2 -> {
                val boxId = splitedInput[1].toInt()
                if (boxId >= 0 && boxId < playboxes.size)
                    blankPlaybox(boxId)
            }
            input.startsWith("blank") && splitedInput.size == 3 -> {
                val boxId = splitedInput[1].toInt()
                val sector = splitedInput[2].toInt()
                if (boxId >= 0 && boxId < playboxes.size)
                    blankPlayboxSector(boxId, sector)
            }
            else -> println("Not recognized command!")
        }
    }

    private fun blankAll() {
        playboxes.forEach { it.blank(pixelFlutInterface) }
    }

    private fun blankPlaybox(boxId: Int) {
        playboxes[boxId].blank(pixelFlutInterface)
    }

    private fun blankPlayboxSector(boxId: Int, sector: Int) {
        playboxes[boxId].blankSector(pixelFlutInterface, sector)
    }
}

class Args(parser: ArgParser) {
    val host by parser.storing("--host", help = "The host of the pixelflut server").default("localhost")
    val port by parser.storing("-p", "--port", help = "The port of the server") { toInt() }.default(1234)
}


