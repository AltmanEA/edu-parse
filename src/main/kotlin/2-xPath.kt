import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

// https://www.freeformatter.com/xpath-tester.html - для тестирования xpath запросов
// https://fedstat.ru/

class Main

val parser =
    XPathFactory
        .newInstance()
        .newXPath()

val doc =
    DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .parse(
            Main::class.java.getResourceAsStream("data.xml")
        )

fun main() {

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
        "//Series/SeriesKey[Value/@value='643']" to { node: Node ->
            val series = node.parentNode
            (series.getText("//Value[@concept='PERIOD']/@value") +
                    series.getText("//ObsValue/@value")).also {
                print(it)
                    }
        }
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