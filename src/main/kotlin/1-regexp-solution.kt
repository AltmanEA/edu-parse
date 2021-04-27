val regexpkt = Reader::class.java
    .getResource("1-regexp.kt")
    .readText()

fun main() {
    println("--- Find variables ---")
    val variables =
        Regex("va[lr] (\\w*)[ :=]")
            .findAll(regexpkt)
            .map {
                it.groups[1]?.value ?: ""
            }.toList()
    println(variables)

    println("--- Check camelStyle ---")
    val camelRegex = Regex("[a-z]+([A-Z][a-z]+)*")
    val camelTests = variables.map {
        camelRegex.matches(it)
    }
    println(camelTests)

    println("--- Check variables init ---")
    val initRegex =
        Regex("(?<=va[lr] )(\\w*)[ :=](?!=.*\\1)", RegexOption.DOT_MATCHES_ALL)
            .findAll(regexpkt)
            .map {
                it.groups.joinToString (
                    separator = " @ ",
                    postfix = "\n"
                ){
                    it?.value?:" "
                }
            }.toList()
    println(initRegex)
}
