import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

// https://www.freeformatter.com/xpath-tester.html - для тестирования xpath запросов
// https://fedstat.ru/

private val parser =
    XPathFactory
        .newInstance()
        .newXPath()

private val doc =
    Reader.doc()

fun main() {

    val extractor = { node: Node ->
        val year = node.getText("../..//Time")
        val month = node.getText("../..//Value[@concept='PERIOD']/@value")
        val value = node.getText("../..//ObsValue/@value")
        "$month $year: $value\n"
    }

    listOf(
        "//CodeList/Name" to { node: Node ->
            node.firstChild.nodeValue
        },
        "//CodeList[@id='s_OKATO']/Code/Description[contains(.,'мск')]" to { node: Node ->
            node.firstChild.nodeValue + " - " +
                    node.parentNode.attributes.getNamedItem("value").nodeValue
        },
        "//CodeList[@id='s_OKATO']/Code/Description[contains(.,'мск')]" to { node: Node ->
            node.getText("../@value") + ": " + node.getText(".")
        },
        "//SeriesKey/Value[@value='643']"
                to extractor,
        "//SeriesKey/Value[@value='643'][../../Attributes/Value[@concept='PERIOD' and not(contains(@value, '-'))]]"
                to extractor,
        "//SeriesKey/Value[@concept='s_OKATO' and @value='643'][../Value[@concept='s_OKVED2' and @value='62']][../../Attributes/Value[@concept='PERIOD' and not(contains(@value, '-'))]]"
                to extractor
    ).map { (query, cursorQuery) ->
        val result = parser
            .compile(query)
            .evaluate(doc, XPathConstants.NODESET) as NodeList
        result.map(cursorQuery).also {
            print(it)
        }
        println()
    }
}


fun <R> NodeList.map(transform: (Node) -> R): List<R> =
    ArrayList<R>().also {
        for (index in 0 until length)
            it.add(
                transform(
                    item(index)
                )
            )
    }

fun Node.getText(query: String) =
    parser
        .compile(query)
        .evaluate(this)
        .toString()