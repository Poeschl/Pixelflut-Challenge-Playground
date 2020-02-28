package io.github.poeschl.pixelflutchallenge.borders

import io.github.poeschl.kixelflut.Pixelflut
import io.github.poeschl.kixelflut.Point
import io.github.poeschl.kixelflut.createRectPixels
import java.awt.Color

class ChallengeArea(private val origin: Point, private val size: Pair<Int, Int>) {

    companion object {
        private val INITAL_COLOR = Color.GRAY
        private val DEFAULT_BACKGROUND_COLOR = Color.BLACK
    }

    private var initialized = false;

    fun draw(pixelflut: Pixelflut) {
        if (!initialized) {
            pixelflut.paintPixelSet(createRectPixels(origin, size, INITAL_COLOR))
            initialized = true
        }
    }

    fun blank(pixelflut: Pixelflut) {
        pixelflut.paintPixelSet(createRectPixels(origin, size, DEFAULT_BACKGROUND_COLOR))
    }

}
