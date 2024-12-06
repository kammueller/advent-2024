@file:Suppress("unused")

import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

const val debug = true

fun main() {
    val timeTaken = measureTimeMillis { december05puzzle() }
    println("Time taken: $timeTaken ms")
}

private fun getFileContent(date: String): String {
    val fileContent = File("inputs/$date.txt").readText().trimIndent()
    return fileContent
}

fun december01puzzle() {
    val fileContent = File("inputs/01-01.txt").readText()
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    val occurrences = mutableMapOf<Int, Int>()
    fileContent.split("\n").forEach {
        if (it.isEmpty()) return@forEach
        val entries = it.split("\\s+".toRegex())
        require(entries.size == 2)
        list1.add(entries[0].toInt())
        val secondEntry = entries[1].toInt()
        list2.add(secondEntry)
        occurrences.compute(secondEntry) { key, value -> (value ?: 0) + 1 }
    }

    list1.sort()
    list2.sort()

    var dist = 0
    var similarity = 0

    for (i in 0 until list1.size) {
        val value = list1[i]
        dist += abs(value - list2[i])
        similarity += value * occurrences.getOrDefault(value, 0)
    }

    println(dist)
    println(similarity)
}

fun december02puzzle() {
    val fileContent = File("inputs/02-01.txt").readText().trim()

    fun isOkay(report: String): Boolean {
        val entries = report.split("\\s+".toRegex()).map { it.toInt() }
        if (entries.size < 2) return true

        var lastInspected = entries[0]
        var descending = entries[1] <= entries[0]
        for (i in 1 until entries.size) {
            val current = entries[i]
            if (descending) {
                if (current >= lastInspected || current < lastInspected - 3) return false
            } else {
                if (current <= lastInspected || current > lastInspected + 3) return false
            }
            lastInspected = current
        }
        return true
    }

    fun okayIfDropSecondOrLater(report: String): Boolean {
        val entries = report.split("\\s+".toRegex()).map { it.toInt() }
        if (entries.size < 2) return true

        var alreadyDampened = false
        var lastInspected = entries[0]
        var descending = entries[1] <= entries[0]
        for (i in 1 until entries.size) {
            val current = entries[i]
            if (descending) {
                if (current >= lastInspected || current < lastInspected - 3) {
                    if (alreadyDampened) {
                        return false
                    } else {
                        alreadyDampened = true
                    }
                } else {
                    lastInspected = current

                }
            } else {
                if (current <= lastInspected || current > lastInspected + 3) {
                    if (alreadyDampened) {
                        return false
                    } else {
                        alreadyDampened = true
                    }
                } else {
                    lastInspected = current
                }
            }
        }
        return true
    }

    fun okayIfDropFirst(report: String): Boolean {
        val entries = report.split("\\s+".toRegex()).map { it.toInt() }
        return isOkay(entries.drop(1).joinToString(" "))
    }

    fun okayIfDropSecond(report: String): Boolean {
        val entries = report.split("\\s+".toRegex()).map { it.toInt() }
        return isOkay("${entries[0]} ${entries.drop(2).joinToString(" ")}")
    }

    fun isAlmostOkay(report: String): Boolean {
        return okayIfDropFirst(report) || okayIfDropSecond(report) || okayIfDropSecondOrLater(report)
    }

    var okay = 0
    var dampenedOk = 0
    fileContent.split("\n").forEach {
        if (isOkay(it)) okay++
        if (isAlmostOkay(it)) dampenedOk++
    }

    println(okay)
    println(dampenedOk)
}

fun december03puzzle() {
    val fileContent = File("inputs/03.txt").readText()

    // returns result and next iterator if a number was found
    fun getNumber(startAt: Int): Pair<Int, Int>? {
        fileContent.substring(startAt, startAt + 3).toIntOrNull()?.let {
            return Pair(it, startAt + 3)
        }
        fileContent.substring(startAt, startAt + 2).toIntOrNull()?.let {
            return Pair(it, startAt + 2)
        }
        fileContent.substring(startAt, startAt + 1).toIntOrNull()?.let {
            return Pair(it, startAt + 1)
        }
        return null
    }

    // returns result, and next iterator (or -1 if end)
    fun findNextMultiplication(startAt: Int): Pair<Int, Int> {
        var it = startAt
        if (it >= fileContent.length - 4) {
            return Pair(0, -1)
        }

        // look for mul(
        if (fileContent.substring(it, it + 4) != "mul(") {
            ++it
            return Pair(0, it)
        }
        it += 4
        // look for first number
        val pair = getNumber(it)
        if (pair == null) {
            return Pair(0, it)
        }
        it = pair.second
        // look for ,
        if (fileContent[it] != ',') {
            return Pair(0, it)
        }
        ++it
        // look for second number
        val pair2 = getNumber(it)
        if (pair2 == null) {
            return Pair(0, it)
        }
        it = pair2.second
        // look for (
        if (fileContent[it] != ')') {
            return Pair(0, it)
        }
        ++it
        // found something
        val result = pair.first * pair2.first
        if (debug) println("mul(${pair.first}, ${pair2.first}) = $result")
        return Pair(result, it)
    }

    fun findDo(startAt: Int): Pair<Boolean, Int> {
        if (startAt + 4 >= fileContent.length) {
            return Pair(false, startAt)
        }

        return if (fileContent.substring(startAt, startAt + 4) == "do()") {
            if (debug) println("do()")
            Pair(true, startAt + 4)
        } else {
            Pair(false, startAt)
        }
    }

    fun findDont(startAt: Int): Pair<Boolean, Int> {
        if (startAt + 7 >= fileContent.length) {
            return Pair(false, startAt)
        }

        return if (fileContent.substring(startAt, startAt + 7) == "don't()") {
            if (debug) println("don't()")
            Pair(true, startAt + 7)
        } else {
            Pair(false, startAt)
        }
    }


    var result1 = 0
    var it = 0
    while (it != -1) {
        val (newResult, nextIt) = findNextMultiplication(it)
        result1 += newResult
        it = nextIt
    }

    println(result1)

    var result2 = 0
    it = 0
    var active = true
    while (it != -1) {
        if (active) {
            val (found, newIt) = findDont(it)
            if (found) {
                active = false
                it = newIt
            }
        } else {
            val (found, newIt) = findDo(it)
            if (found) {
                active = true
                it = newIt
            }
        }

        val (newResult, nextIt) = findNextMultiplication(it)
        if (active) {
            result2 += newResult
        }
        it = nextIt
    }

    println(result2)
}

fun december04puzzle() {
    val rows = File("inputs/04.txt").readText().trim().split("\n")
    val length = rows.size
    val width = rows[0].toCharArray().size

    var found = 0
    // forward and backward
    rows.forEach { row ->
        for (i in 0..width - 4) {
            val substring = row.substring(i, i + 4)
            if (substring == "XMAS" || substring == "SAMX") {
                ++found
            }
        }
    }
    if (debug) println("horizontal: $found")
    // up and down
    for (i in 0..length - 4) {
        for (j in 0 until width) {
            val substring = "${rows[i][j]}${rows[i + 1][j]}${rows[i + 2][j]}${rows[i + 3][j]}"
            if (substring == "XMAS" || substring == "SAMX") {
                ++found
            }
        }
    }
    if (debug) println("and vertical: $found")
    // diagonals \
    for (i in 0..length - 4) {
        for (j in 0..width - 4) {
            val substring = "${rows[i][j]}${rows[i + 1][j + 1]}${rows[i + 2][j + 2]}${rows[i + 3][j + 3]}"
            if (substring == "XMAS" || substring == "SAMX") {
                ++found
            }
        }
    }
    if (debug) println("and right-down : $found")
    // diagonals /
    for (i in 0..length - 4) {
        for (j in 3 until width) {
            val substring = "${rows[i][j]}${rows[i + 1][j - 1]}${rows[i + 2][j - 2]}${rows[i + 3][j - 3]}"
            if (substring == "XMAS" || substring == "SAMX") {
                ++found
            }
        }
    }
    if (debug) println("and right-up : $found")

    println(found)

    // Part 2
    fun isAnXMas(i: Int, j: Int): Boolean {
        require(i >= 0 && i < length - 2)
        require(j >= 0 && j < width - 2)
        return rows[i + 1][j + 1] == 'A' &&
            ((rows[i][j] == 'M' && rows[i + 2][j + 2] == 'S') || (rows[i][j] == 'S' && rows[i + 2][j + 2] == 'M')) &&
            ((rows[i + 2][j] == 'M' && rows[i][j + 2] == 'S') || (rows[i + 2][j] == 'S' && rows[i][j + 2] == 'M'))
    }

    found = 0
    for (i in 0 until length - 2) {
        for (j in 0 until width - 2) {
            if (isAnXMas(i, j)) {
                ++found
            }
        }
    }
    println(found)
}

fun december05puzzle() {
    val fileContent = getFileContent("05")
    val (rules, tests) = fileContent.split("\n\n")

    val before = mutableMapOf<Int, List<Int>>()
    rules.split("\n").forEach { rule ->
        val (predecessor, page) = rule.split("|")
        before.compute(page.toInt()) { _, list -> (list ?: emptyList()) + predecessor.toInt() }
    }

    var result = 0
    tests.split('\n').forEach { test ->
        val pages = test.split(",").map { it.toInt() }
        val nrPages = pages.size
        var correct = true
        for (i in 0 until nrPages) {
            val page = pages[i]
            val pagesAfter = pages.drop(i)
            val predecessors = before[page] ?: emptyList()
            if (pagesAfter.any { it in predecessors }) {
                correct = false
                break
            }
        }
        if (correct) {
            println("correct: $test")
            result += pages[nrPages / 2]
        }
    }
    println(result)
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun december06puzzle() {
    val fileContent = getFileContent("06")
    val rows = fileContent.split("\n")
    val length = rows.size
    val width = rows[0].length

    val obstacles = Array(length) { Array(width) { false } }
    var start: Pair<Int, Int>? = null

    for (i in 0 until length) {
        for (j in 0 until width) {
            obstacles[i][j] = rows[i][j] == '#'
            if (rows[i][j] == '^') {
                start = Pair(i, j)
            }
        }
    }
    require(start != null)
    if (debug) println("start: ${start.first} x ${start.second}")

    var pos: Pair<Int, Int> = start
    var looking = Direction.UP
    var visited = Array(length) { Array(width) { mutableSetOf<Direction>() } }

    fun withinBounds(i: Int, j: Int): Boolean {
        return (0 until length).contains(i) && (0 until width).contains(j)
    }

    fun isObstacle(i: Int, j: Int): Boolean {
        return withinBounds(i, j) && obstacles[i][j]
    }

    fun beenHere(i: Int, j: Int): Boolean {
        require(withinBounds(i, j))
        return visited[i][j].contains(looking)
    }

    fun getNextPos(i: Int, j: Int): Pair<Int, Int> {
        val (newX, newY) = when (looking) {
            Direction.UP -> Pair(i - 1, j)
            Direction.DOWN -> Pair(i + 1, j)
            Direction.LEFT -> Pair(i, j - 1)
            Direction.RIGHT -> Pair(i, j + 1)
        }
        if (isObstacle(newX, newY)) {
            looking = when (looking) {
                Direction.UP -> Direction.RIGHT
                Direction.DOWN -> Direction.LEFT
                Direction.LEFT -> Direction.UP
                Direction.RIGHT -> Direction.DOWN
            }
            return getNextPos(i, j)
        }
        return Pair(newX, newY)
    }

    fun markVisited(pos: Pair<Int, Int>, allowDoubles: Boolean = false) {
        val visitedSet = visited[pos.first][pos.second]
        require(allowDoubles || !visitedSet.contains(looking))
        visitedSet.add(looking)
    }

    while (withinBounds(pos.first, pos.second)) {
        markVisited(pos)
        pos = getNextPos(pos.first, pos.second)
        if (debug) println("walked to ${pos.first} x ${pos.second}")
    }

    val nrVisited = visited.sumOf { row -> row.count { !it.isEmpty() } }
    println(nrVisited)
    require(debug || nrVisited == 5404)

    // Part 2

    fun reset() {
        pos = start
        looking = Direction.UP
        visited = Array(length) { Array(width) { mutableSetOf<Direction>() } }
    }

    fun makesLoop(i: Int, j: Int): Boolean {
        require(!obstacles[i][j])
        obstacles[i][j] = true

        while (withinBounds(pos.first, pos.second) && !beenHere(pos.first, pos.second)) {
            markVisited(pos, true)
            pos = getNextPos(pos.first, pos.second)
        }

        obstacles[i][j] = false
        val isLoop = withinBounds(pos.first, pos.second)
        if (debug && isLoop) println("loop found with obstacle $i x $j")
        return isLoop
    }

    val candidates = mutableListOf<Pair<Int, Int>>()
    for (i in 0 until length) {
        for (j in 0 until width) {
            if (visited[i][j].isNotEmpty()) {
                candidates.add(Pair(i, j))
            }
        }
    }
    require(candidates.size == nrVisited)

    var nrOptions = 0
    candidates.forEach { (i, j) ->
        reset()
        if (makesLoop(i, j)) {
            ++nrOptions
        }
    }
    println(nrOptions)
}
