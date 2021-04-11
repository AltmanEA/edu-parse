import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

val reader = XMLInputFactory
    .newInstance()
    .createXMLStreamReader(
        Main::class.java.getResourceAsStream("data_part.xml")
    )
var result = ""

fun main() {
    while (reader.hasNext())
        when (reader.next()){
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "CodeLists")
                    readLists()
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "CodeLists")
                    break
        }
    print(result)
}


fun readLists() {
    while (reader.hasNext())
        when (reader.next()) {
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "CodeList")
                    readList()
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "CodeLists")
                    return
        }
}

fun readList() {
    while (reader.hasNext())
        when (reader.next()) {
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "Name")
                    result += reader.elementText + "\n"
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "CodeList")
                    return
        }
}