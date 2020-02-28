package io.github.poeschl.pixelflutchallenge.borders

import io.github.poeschl.kixelflut.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.awt.Color


class Playbox(private val origin: Point, private val size: Pair<Int, Int>) {

    companion object {
        private val DEFAULT_BORDER_COLOR = Color.WHITE
        private val DEFAULT_BACKGROUND_COLOR = Color.BLACK
        private const val SPLIT_COUNT = 2
    }

    private var drawColor = DEFAULT_BORDER_COLOR

    fun draw(pixelFlutInterface: Pixelflut) {
        drawBorder(pixelFlutInterface)
        drawInternalSplit(pixelFlutInterface)
    }

    fun blank(pixelFlutInterface: Pixelflut) = runBlocking {
        launch {
            blankSector(pixelFlutInterface, 0)
        }
        launch {
            blankSector(pixelFlutInterface, 1)
        }
        launch {
            blankSector(pixelFlutInterface, 2)
        }
        launch {
            blankSector(pixelFlutInterface, 3)
        }
    }

    fun blankSector(pixelFlutInterface: Pixelflut, sector: Int) {
        val origin = when (sector) {
            0 -> origin.plus(Point(1, 1))
            1 -> origin.plus(Point(1 + size.first / SPLIT_COUNT, 1))
            2 -> origin.plus(Point(1 + size.first / SPLIT_COUNT, 1 + size.second / SPLIT_COUNT))
            3 -> origin.plus(Point(1, 1 + size.second / SPLIT_COUNT))
            else -> origin.plus(Point(1, 1))
        }

        val blankSize = when (sector) {
            0 -> Pair(size.first / 2 - 1, size.second / 2 - 1)
            1 -> Pair(size.first / 2 - 2, size.second / 2 - 1)
            2 -> Pair(size.first / 2 - 2, size.second / 2 - 2)
            3 -> Pair(size.first / 2 - 1, size.second / 2 - 2)
            else -> Pair(size.first / 2 - 1, size.second / 2 - 1)
        }
        pixelFlutInterface.paintPixelSet(
            createRectPixels(origin, blankSize, DEFAULT_BACKGROUND_COLOR)
        )
    }

    private fun drawBorder(pixelFlutInterface: Pixelflut) = runBlocking {
        launch {
            pixelFlutInterface.paintPixelSet(createHorizontalPixels(origin, size.first, drawColor))
        }
        launch {
            pixelFlutInterface.paintPixelSet(createVerticalPixels(origin, size.second, drawColor))
        }

        launch {
            pixelFlutInterface.paintPixelSet(
                createHorizontalPixels(
                    origin.plus(Point(0, size.second - 1)),
                    size.first,
                    drawColor
                )
            )
        }
        launch {
            pixelFlutInterface.paintPixelSet(
                createVerticalPixels(
                    origin.plus(Point(size.first - 1, 0)),
                    size.second,
                    drawColor
                )
            )
        }
    }

    private fun drawInternalSplit(pixelFlutInterface: Pixelflut) {
        val xSplit = size.first / SPLIT_COUNT
        val ySplit = size.second / SPLIT_COUNT

        runBlocking {
            for (i: Int in 0 until SPLIT_COUNT) {
                launch {
                    pixelFlutInterface.paintPixelSet(
                        createVerticalPixels(
                            origin.plus(Point(xSplit * i, 0)),
                            size.second,
                            drawColor
                        )
                    )
                }
            }
            for (i: Int in 0 until SPLIT_COUNT) {
                launch {
                    pixelFlutInterface.paintPixelSet(
                        createHorizontalPixels(
                            origin.plus(Point(0, ySplit * i)),
                            size.first,
                            drawColor
                        )
                    )
                }
            }
        }
    }
}
