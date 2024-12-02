import java.io.File
import kotlin.math.abs

fun december01puzzle01() {
    val fileContent = File("inputs/01-01.txt").readText()
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    fileContent.split("\n").forEach {
        if (it.isEmpty()) return@forEach
        val entries = it.split("\\s+".toRegex())
        require(entries.size == 2)
        list1.add(entries[0].toInt())
        list2.add(entries[1].toInt())
    }

    list1.sort()
    list2.sort()

    var dist = 0

    for (i in 0 until list1.size) {
        dist += abs(list1[i] - list2[i])
    }

    println(dist)
}

    fun main() {
        december01puzzle01()
    }
