typealias Coordinates = Pair<Int, Int>

fun december08puzzle() {
    val fileContent = getFileContent("08")
    val frequencies = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val rows = fileContent.split('\n').map { it.toCharArray().toList() }
    val length = rows.size
    val width = rows[0].size

    fun Coordinates.isValid(): Boolean =
        first in (0 until length) && second in (0 until width)

    operator fun Coordinates.plus(other: Coordinates): Coordinates =
        Pair(first + other.first, second + other.second)

    operator fun Coordinates.minus(other: Coordinates): Coordinates =
        Pair(first - other.first, second - other.second)


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
            (i + 1 until list.size).forEach { j ->
                result += Pair(list[i], list[j])
            }
        }
        return result
    }

    fun getAntiNodesForStations(a: Coordinates, b: Coordinates): List<Coordinates> {
        val diff = b - a
        val antiNodes = listOf(a - diff, b + diff)

        return antiNodes.filter { it.isValid() }
    }

    fun getHarmonicAntiNodesForStations(a: Coordinates, b: Coordinates): List<Coordinates> {
        val diff = b - a
        var harmonicAntiNodes = mutableListOf<Coordinates>()
        var candidate = a
        // go up
        while (candidate.isValid()) {
            harmonicAntiNodes.add(candidate)
            candidate = candidate - diff
        }
        // go down
        candidate = a + diff
        while (candidate.isValid()) {
            harmonicAntiNodes.add(candidate)
            candidate = candidate + diff
        }

        return harmonicAntiNodes
    }

    val antiNodes = Array(length) { Array(width) { false } }
    val harmonicsAntiNodes = Array(length) { Array(width) { false } }
    for (f in frequencies) {
        val senders = findAllSenders(f)
        getCombinations(senders).forEach { (sender1, sender2) ->
            val newAntiNodes = getAntiNodesForStations(sender1, sender2)
            newAntiNodes.forEach { (i, j) -> antiNodes[i][j] = true }

            getHarmonicAntiNodesForStations(sender1, sender2).forEach { (i, j) ->
                harmonicsAntiNodes[i][j] = true
            }
        }
    }

    if (debug) {
        for (i in 0 until length) {
            for (j in 0 until width) {
                if (antiNodes[i][j]) {
                    print('#')
                } else if (harmonicsAntiNodes[i][j]) {
                    print('@')
                } else {
                    print('.')
                }
            }
            println()
        }
        println()
    }

    println(antiNodes.sumOf { row -> row.count { it } })
    println(harmonicsAntiNodes.sumOf { row -> row.count { it } })
}
