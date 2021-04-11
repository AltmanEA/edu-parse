import Reader.Companion.reader
import javax.xml.stream.XMLStreamReader

private var result = ""

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

private fun readLists() {
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

private fun readList() {
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