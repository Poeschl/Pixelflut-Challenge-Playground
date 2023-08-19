package xyz.poeschl.challengeplayground

class CliInterface(private val playgroundPainter: PlaygroundDrawer, private val restInterface: RestInterface) {

    private var inputRunning = true;

    fun start() {
        displayHelp()

        var input: String
        do {
            print("> ")
            input = readLine() ?: ""
            handleInput(input)
        } while (inputRunning)
    }

    private fun displayHelp() {
        println("Available commands:")
        println("quit -> Quit application")
        println("blank -> Wipes the whole screen")
        println("blank center-> Wipes the centered challenge area")
        println("blank <id> -> Wipes one playground (left to right starting at 0 from top left)")
        println("blank <id> <sector> -> Wipes one playground sector (sector are counted clockwise, starting top left)")
    }

    private fun handleInput(input: String) {
        val splitedInput = input.split(' ')
        when {
            input == "quit" -> {
                inputRunning = false
                restInterface.stop()
                playgroundPainter.stop()
            }
            input == "dummy" -> playgroundPainter.dummy()
            input == "blank" -> playgroundPainter.blankAll()
            input == "blank center" -> playgroundPainter.blankCenter()
            input.startsWith("blank") && splitedInput.size == 2 -> {
                val boxId = splitedInput[1].toInt()
                if (boxId >= 0 && boxId < playgroundPainter.numberPlayboxes())
                    playgroundPainter.blankPlaybox(boxId)
            }
            input.startsWith("blank") && splitedInput.size == 3 -> {
                val boxId = splitedInput[1].toInt()
                val sector = splitedInput[2].toInt()
                if (boxId >= 0 && boxId < playgroundPainter.numberPlayboxes()
                    && sector >= 0 && sector < playgroundPainter.numberSections()
                )
                    playgroundPainter.blankPlayboxSector(boxId, sector)
            }
            else -> println("Not recognized command!")
        }
    }
}
