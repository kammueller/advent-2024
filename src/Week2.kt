fun december08puzzle() {
    val fileContent = getFileContent("08")
    val frequencies = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val rows = fileContent.split('\n')
    val length = rows.size
    val width = rows[0].toCharArray().size
    var nrAntiNodes = 0

    println(nrAntiNodes)
}
