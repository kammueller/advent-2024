@file:Suppress("unused")

import java.io.File
import kotlin.math.abs

fun main() {
    december03puzzle()
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
        println("mul(${pair.first}, ${pair2.first}) = $result")
        return Pair(result, it)
    }

    fun findDo(startAt: Int): Pair<Boolean, Int> {
        if (startAt + 4 >= fileContent.length) {
            return Pair(false, startAt)
        }

        return if (fileContent.substring(startAt, startAt + 4) == "do()") {
            println("do()")
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
            println("don't()")
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
        }else {
            val (found, newIt) = findDo(it)
            if (found) {
                active = true
                it = newIt
            }
        }

        val (newResult, nextIt) = findNextMultiplication(it)
        if(active) {
            result2 += newResult
        }
        it = nextIt
    }

    println(result2)
}
