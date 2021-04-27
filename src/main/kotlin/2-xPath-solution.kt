import org.w3c.dom.Element
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

private val parser =
    XPathFactory
        .newInstance()
        .newXPath()

private val doc =
    DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .parse(
            Reader::class.java.getResourceAsStream("data_lab.xml")
        )

fun main() {

    println("--- city codes ---")
    val codeCondition = listOf("Федерация", "Омск", "Москва")
        .map { "contains(.,'$it')" }
        .joinToString(" or ") { it }
    val codeNodes =
        parser
            .compile("//CodeList[@id='s_OKATO']/Code/Description[$codeCondition]")
            .evaluate(doc, XPathConstants.NODESET) as NodeList
    val codes =
        codeNodes
            .map { node ->
                node.firstChild.nodeValue to
                        node.parentNode.attributes.getNamedItem("value").nodeValue
            }
    println(codes)

    println("--- good codes ---")
    val goodCodeNodes =
        parser
            .compile("//SeriesKey/Value[@concept='s_grtov']")
            .evaluate(doc, XPathConstants.NODESET) as NodeList
    val goodCodes =
        goodCodeNodes
            .map {
                it.attributes.getNamedItem("value").nodeValue
            }.toSet()
    val goodCondition = "contains(\"${goodCodes.joinToString(" ") { it }}\", @value)"
    val goodNodes =
        parser
            .compile("//CodeList[@id='s_grtov']/Code[$goodCondition]/Description")
            .evaluate(doc, XPathConstants.NODESET) as NodeList
    val goods =
        goodNodes
            .map {
                (it.parentNode as Element).getAttribute("value") to
                it.firstChild.nodeValue
            }

    println(goods)
}