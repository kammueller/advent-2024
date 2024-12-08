typealias Coordinates = Pair<Int, Int>

fun december08puzzle() {
    val fileContent = getFileContent("08")
    val frequencies = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val rows = fileContent.split('\n').map { it.toCharArray().toList() }
    val length = rows.size
    val width = rows[0].size

    fun Coordinates.isValid(): Boolean =
        first in (0 until length) && second in (0 until width)

    fun findAllSenders(f: Char): List<Coordinates> {
        val result = mutableListOf<Coordinates>()
        for (i in 0 until length) {
            for (j in 0 until width) {
                if (rows[i][j] == f) {
                    result += Pair(i, j)
                }
            }
        }
        return result
    }

    fun <T> getCombinations(list: List<T>): List<Pair<T, T>> {
        val result = mutableListOf<Pair<T, T>>()
        list.indices.forEach { i ->
            (i+1 until list.size).forEach{ j ->
                result += Pair(list[i], list[j])
            }
        }
        return result
    }

    fun getAntiNodesForStations(a: Coordinates, b:Coordinates): List<Coordinates> {
        val (x1, y1) = a
        val (x2, y2) = b
        val diffX = x2-x1
        val diffY = y2-y1

        val antiNodes = listOf(Pair(x1-diffX, y1-diffY), Pair(x2+diffX, y2+diffY))

        return antiNodes.filter {it.isValid()}
    }

    val antiNodes = Array(length) { Array(width) { false } }
    for (f in frequencies) {
        val senders = findAllSenders(f)
        getCombinations(senders).forEach { (sender1, sender2) ->
            val newAntiNodes = getAntiNodesForStations(sender1, sender2)
            newAntiNodes.forEach{ (i,j) -> antiNodes[i][j] = true}
        }
    }

    if (debug) {
        for (i in 0 until length) {
            for (j in 0 until width) {
               print(if(antiNodes[i][j]) '#' else '.' )
            }
            println()
        }
        println()
    }

    println(antiNodes.sumOf { row -> row.count { it } })
}
