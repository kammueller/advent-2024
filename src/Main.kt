import java.io.File
import kotlin.system.measureTimeMillis

const val debug = false

fun main() {
    val timeTaken = measureTimeMillis { december08puzzle() }
    println("Time taken: $timeTaken ms")
}

internal fun getFileContent(date: String): String {
    val fileContent = File("inputs/$date.txt").readText().trimIndent()
    return fileContent
}
