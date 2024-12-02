@file:Suppress("unused")

import java.io.File
import kotlin.math.abs

fun main() {
    december02puzzle()
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

    fun okayIfDropSecond (report: String): Boolean {
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
