// https://regex101.com/ - для построения регулярных выражений

fun main() {
    println("--- Split example ---\n")
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
        println(result.last())
    }

    println("\n--- Replace example ---\n")
    val replaceExample = "Федор Михайлович Достоевский и Лев Николаевич Толстой. И Александр Сергеевич Пушкин, конечно."
    val replaceRegex = Regex("([А-Я][а-я]+ ){2}[А-Я][а-я]+") // Диапазоны, квантификаторы
    println(replaceExample.replace(replaceRegex) {
        val fio = it.value.split(" ")
        "${fio[2]} ${fio[0][0]}. ${fio[1][0]}."
    })

    println("\n--- Find example ---\n")
    val findExample = "<div class='x'> text <b> bold </b> text </div>"
    listOf(
        Regex("<.*>"),                      // Точка, квантификатор *
        Regex("<[^>]*>"),                   // Отрицание
        Regex("<.*?>"),                     // Ленивая квантификация
        Regex("<(.*?)>.*</\\1>"),           // Группы
        Regex("<(\\w*?)(.*?)>(.*)</\\1>"),  // Классы символов
    ).forEach { regex ->
        println("--- Pattern: ${regex.pattern} --- ")
        regex.findAll(findExample).forEach {
            print("---")
            it.groups.forEach { matchGroup ->
                println("\t range - ${matchGroup?.range}; value - |${matchGroup?.value}|")
            }
        }
    }
}