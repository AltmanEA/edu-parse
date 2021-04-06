fun main() {
    val splitExample = "Пример первый. Разделяем на слова, учитываем знаки препинания."
    listOf(
        Regex(" "),             // Понятие шаблона
        Regex("[,. ]"),         // Или
        Regex("[,.]? "),        // Квантификатор ?, второй вариант или.
        Regex("[[,.]? |.]"),    // Второй вариант или
        Regex("[[,|.]? |.]+")   // Ревнивания квантификация
    ).forEach { regex ->
        println("--- Pattern: ${regex.pattern} --- ")
        val match = regex.findAll(splitExample).toList()
        val result = splitExample.split(regex)
        result.zip(match).forEach {
            print("${it.first}\n\t\t")
            it.second.groups.forEach { matchGroup ->
                println("range - ${matchGroup?.range}; value - |${matchGroup?.value}|")
            }
        }
    }

    println("Replace example")
    val replaceExample = "Федор Михайлович Достоевский и Лев Николаевич Толстой. И Александр Сергеевич Пушкин, конечно."
    val replaceRegex = Regex("([А-Я][а-я]+ ){2}[А-Я][а-я]+") // Диапазоны, квантификаторы
    println(replaceExample.replace(replaceRegex) {
        val fio = it.value.split(" ")
        "${fio[2]} ${fio[0][0]}. ${fio[1][0]}."
    })


}