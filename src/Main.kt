import java.io.File
import kotlin.math.abs

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

fun main() {
    december01puzzle()
}
