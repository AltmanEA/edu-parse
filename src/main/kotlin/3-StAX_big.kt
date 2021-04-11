import javax.xml.stream.XMLStreamReader

private class Point(
    var year: Int = 0,
    var period: String = "",
    var value: Float = 0f
){
    override fun toString(): String =
        "$year $period: $value\n"

}

private val data = ArrayList<Point>()

fun main() {
    while (reader.hasNext())
        if (reader.next() == XMLStreamReader.START_ELEMENT)
            if (reader.localName == "Series")
                checkSeriesKey()
    print(data)
}

fun checkSeriesKey() {
    while (reader.hasNext())
        when (reader.next()) {
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "SeriesKey")
                    checkValue()
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "Series")
                    return
        }
}

fun checkValue() {
    var rightValues = 0
    while (reader.hasNext())
        when (reader.next()) {
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "Value") {
                    if ((reader.getAttributeValue(0) == "s_OKATO") and
                        (reader.getAttributeValue(1) == "643")
                    ) rightValues++
                    if ((reader.getAttributeValue(0) == "s_OKVED2") and
                        (reader.getAttributeValue(1) == "62")
                    ) rightValues++
                    if(rightValues==2) {
                        readSeries()
                        return
                    }
                }
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "SeriesKey")
                    return
        }
}

fun readSeries() {
    val point = Point()
    while (reader.hasNext())
        when (reader.next()) {
            XMLStreamReader.START_ELEMENT ->
                when (reader.localName) {
                    "Attributes" -> readAttributes(point)
                    "Obs" -> readObs(point)
                }
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "Series"){
                    if(point.period!="")
                        data.add(point)
                    return
                }

        }

}

private fun readAttributes(point: Point) {
    while (reader.hasNext())
        when (reader.next()) {
            XMLStreamReader.START_ELEMENT ->
                if (reader.localName == "Value")
                    if (reader.getAttributeValue(0) == "PERIOD") {
                        val period = reader.getAttributeValue(1)
                        if (!period.contains("-"))
                            point.period = period
                    }
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "Attributes")
                    return
        }
}

private fun readObs(point: Point) {
    while (reader.hasNext())
        when (reader.next()) {
            XMLStreamReader.START_ELEMENT ->
                when (reader.localName) {
                    "Time" ->
                        point.year = reader.elementText.toInt()
                    "ObsValue" ->
                        point.value = reader.getAttributeValue(0)
                            .replace(",", ".").toFloat()
                }
            XMLStreamReader.END_ELEMENT ->
                if (reader.localName == "ObsValue")
                    return
        }
}