import Reader.Companion.reader
import javax.xml.stream.XMLStreamReader

fun interface State {
    fun eval(eventType: Int)
}

private val start: State = State {
    when (it) {
        XMLStreamReader.START_ELEMENT ->
            if (reader.localName == "CodeLists")
                current = lists
    }
}
private val lists: State = State {
    when (it) {
        XMLStreamReader.START_ELEMENT ->
            if (reader.localName == "CodeList")
                current = codes
        XMLStreamReader.END_ELEMENT ->
            if (reader.localName == "CodeLists")
                current = start
    }
}
private val codes: State = State {
    when (it) {
        XMLStreamReader.START_ELEMENT ->
            if (reader.localName == "Name")
                result += reader.elementText + "\n"
        XMLStreamReader.END_ELEMENT ->
            if (reader.localName == "CodeList")
                current = lists
    }
}

private var result = ""
private var current: State = start

fun main() {
    while (reader.hasNext())
        current.eval(reader.next())
    print(result)
}