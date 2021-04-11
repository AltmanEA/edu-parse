import javax.xml.stream.XMLInputFactory

class Reader {
    companion object{
        val reader = XMLInputFactory
            .newInstance()
            .createXMLStreamReader(
                Reader::class.java.getResourceAsStream("data_part.xml")
            )
    }
}