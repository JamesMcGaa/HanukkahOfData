fun main() {
    val customers = loadOpenCsv("data/noahs-customers.csv")
    val orders = loadOpenCsv("data/noahs-orders.csv")
    val products = loadOpenCsv("data/noahs-products.csv")
    val orders2017 = orders.filter {it["shipped"]!!.contains("2017")}
    orders2017.forEach {
        val customerId = it["customerid"]!!
        val sku = it["sku"]!!
        val customer = customers.filter { it["customerid"]!! == customerId }.first()
        val product = products.filter { it["sku"]!! == sku }.first()
        val name = customer["name"]!!
        if (getInitials(name) == "JD") {
            if (product["desc"]!!.contains("bagel") || product["desc"]!!.contains("Bagel")){
                println(customer)
            }
        }
    }
//    println(orders2017)
}

fun getInitials(name: String): String {
    val words = name.split(" ").toMutableList()
    val badLastWords = setOf("Jr.")
    if (badLastWords.contains(words.last()) ||
        (words.last().toList().toSet() - setOf('I', 'V')).isEmpty()
    ) {
        words.removeLast()
    }
    return words.first().first().uppercase() + words.last().first().uppercase()
}

