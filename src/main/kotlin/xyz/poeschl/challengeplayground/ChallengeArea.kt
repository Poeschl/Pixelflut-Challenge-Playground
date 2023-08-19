package xyz.poeschl.challengeplayground

import xyz.poeschl.kixelflut.Pixelflut
import xyz.poeschl.kixelflut.Point
import xyz.poeschl.kixelflut.createRectPixels
import java.awt.Color

class ChallengeArea(private val origin: Point, private val size: Pair<Int, Int>) {

    companion object {
        private val INITAL_COLOR = Color.GRAY
        private val DEFAULT_BACKGROUND_COLOR = Color.BLACK
    }

    private var initialized = false;

    fun draw(pixelflut: Pixelflut) {
        if (!initialized) {
            pixelflut.drawPixels(createRectPixels(origin, size, INITAL_COLOR))
            initialized = true
        }
    }

    fun blank(pixelflut: Pixelflut) {
        pixelflut.drawPixels(createRectPixels(origin, size, DEFAULT_BACKGROUND_COLOR))
    }

}
