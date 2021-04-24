import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.stream.XMLInputFactory

class Reader {
    companion object {
        fun reader() = XMLInputFactory
            .newInstance()
            .createXMLStreamReader(
                Reader::class.java.getResourceAsStream("data_part.xml")
            )
        fun doc() =
            DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(
                    Reader::class.java.getResourceAsStream("data_part.xml")
                )
    }
}