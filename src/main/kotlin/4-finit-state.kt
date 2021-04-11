import State.Companion.current
import State.Companion.result
import javax.xml.stream.XMLStreamReader

interface State {
    companion object {
        var current: State = start
        var result = ""
    }

    fun eval(eventType: Int, reader: XMLStreamReader)
}

val start = Start()
val lists = Lists()
val codes = Codes()

class Start : State {
    override fun eval(eventType: Int, reader: XMLStreamReader) {
        when (eventType) {
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "CodeLists")
                    current = lists
        }
    }
}

class Lists : State {
    override fun eval(eventType: Int, reader: XMLStreamReader) {
        when (eventType) {
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "CodeList")
                    current = codes
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "CodeLists")
                    current = start
        }
    }
}

class Codes : State {
    override fun eval(eventType: Int, reader: XMLStreamReader) {
        when (eventType) {
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "Name")
                    result += reader.elementText + "\n"
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "CodeList")
                    current = lists
        }
    }
}


fun main() {
    while (reader.hasNext())
        current.eval(reader.next(), reader)
    print(result)
}