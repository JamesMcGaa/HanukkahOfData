import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.File
import java.io.FileReader

const val CLEANPHONE = "cleanphone"
const val IMPLIEDPHONE = "impliedphone"
val removedSet = mutableSetOf<String>()
fun main() {
    val rows = loadOpenCsv("data/noahs-customers.csv")
    rows.forEach { setAdjName(it) }
    println(rows.filter { it[IMPLIEDPHONE]!! == it[CLEANPHONE]!! }.first()["phone"])
}

fun loadOpenCsv(csvPath: String): MutableList<MutableMap<String, String>> {
    val csvReader = CSVReaderBuilder(FileReader(csvPath))
        .withCSVParser(CSVParserBuilder().withSeparator(',').build())
        .build()
    val rows = mutableListOf<MutableMap<String, String>>()
    csvReader.use { reader ->
        val header = reader.readNext()
        var line = reader.readNext()
        while (line != null) {
            val row = mutableMapOf<String, String>()
            line.forEachIndexed { index, value ->
                val columnName = header[index]
                row[columnName] = value
            }
            rows.add(row)
            line = reader.readNext()
        }
    }
    return rows
}

// Does not account for nested commas within quotes
fun loadCsv(csvPath: String): MutableList<MutableMap<String, String>> {
    val lines = File(csvPath).readLines()
    val header = mutableListOf("index") + lines[0].split(",")
    val rows = mutableListOf<MutableMap<String, String>>()
    lines.subList(1, lines.lastIndex + 1).forEach { line ->
        val row = mutableMapOf<String, String>()
        line.split(",").forEachIndexed { index, value ->
            val columnName = header[index]
            row[columnName] = value
        }
        rows.add(row)
    }
    return rows
}

fun setAdjName(row: MutableMap<String, String>) {
    row[CLEANPHONE] = row["phone"]!!.filter { it.isDigit() }
    row[IMPLIEDPHONE] = convertChars(getModifiedName(row["name"]!!))
}

val CHAR_MAP = mutableMapOf(
    'a' to '2',
    'b' to '2',
    'c' to '2',

    'd' to '3',
    'e' to '3',
    'f' to '3',

    'g' to '4',
    'h' to '4',
    'i' to '4',

    'j' to '5',
    'k' to '5',
    'l' to '5',

    'm' to '6',
    'n' to '6',
    'o' to '6',

    'p' to '7',
    'q' to '7',
    'r' to '7',
    's' to '7',

    't' to '8',
    'u' to '8',
    'v' to '8',

    'w' to '9',
    'x' to '9',
    'y' to '9',
    'z' to '9',
)

fun convertChars(modifiedName: String): String {
    var converted = ""
    modifiedName.forEach {
        converted += CHAR_MAP[it]!!
    }
    return converted
}

fun getModifiedName(name: String): String {
    val words = name.split(" ").toMutableList()
    val badLastWords = setOf("Jr.")
    if (badLastWords.contains(words.last()) ||
        (words.last().toList().toSet() - setOf('I', 'V')).isEmpty()
    ) {
        val removed = words.removeLast()
        removedSet.add(removed)
    }
    return words.last().map { it.lowercaseChar() }.joinToString("")
}
