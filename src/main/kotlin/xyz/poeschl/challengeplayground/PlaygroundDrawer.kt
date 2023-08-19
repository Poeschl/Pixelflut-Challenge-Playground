package xyz.poeschl.challengeplayground

import xyz.poeschl.kixelflut.Painter
import xyz.poeschl.kixelflut.Pixelflut
import xyz.poeschl.kixelflut.Point
import xyz.poeschl.kixelflut.createRectPixels
import java.awt.Color
import kotlin.math.floor

class PlaygroundDrawer(private val host: String, private val port: Int) : Painter() {
    companion object {
        private const val SPLIT_COUNT = 3
        private val FINAL_CHALLENGE_POSITION = floor(SPLIT_COUNT / 2.0).toInt()
    }

    private val pixelFlutInterface = Pixelflut(host, port)
    private val displaySize = pixelFlutInterface.getScreenSize()
    private val playboxes = mutableListOf<Playbox>()
    private lateinit var challengeArea: ChallengeArea

    override fun init() {
        println("Detected size $displaySize")

        val xSlice = displaySize.first / SPLIT_COUNT
        val ySlice = displaySize.second / SPLIT_COUNT

        for (y: Int in 0 until SPLIT_COUNT) {
            for (x: Int in 0 until SPLIT_COUNT) {
                if (x == FINAL_CHALLENGE_POSITION && y == FINAL_CHALLENGE_POSITION) {
                    challengeArea = ChallengeArea(Point(xSlice * x, ySlice * y), Pair(xSlice, ySlice))
                } else {
                    playboxes.add(Playbox(Pixelflut(host, port), Point(xSlice * x, ySlice * y), Pair(xSlice, ySlice)))
                }
            }
        }
        println("Setup ${playboxes.size} playboxes")
    }

    override fun render() {
        challengeArea.draw(pixelFlutInterface)
        playboxes.parallelStream().forEach { it.draw() }
    }

    override fun afterStop() {
        playboxes.parallelStream().forEach { it.close() }
        pixelFlutInterface.close()
    }

    fun numberPlayboxes(): Int {
        return playboxes.size
    }

    fun numberSections(): Int {
        return 4
    }

    fun blankCenter() {
        challengeArea.blank(pixelFlutInterface)
    }

    fun blankAll() {
        playboxes.forEach { it.blank() }
    }

    fun blankPlaybox(boxId: Int) {
        playboxes[boxId].blank()
    }

    fun blankPlayboxSector(boxId: Int, sector: Int) {
        playboxes[boxId].blankSector(sector)
    }

    fun dummy() {
        pixelFlutInterface.drawPixels(
            createRectPixels(
                Point(0, 0),
                pixelFlutInterface.getScreenSize(),
                Color.CYAN
            )
        )
    }
}


