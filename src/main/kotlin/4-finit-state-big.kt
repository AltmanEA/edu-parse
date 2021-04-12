import Reader.Companion.reader
import javax.xml.stream.XMLStreamReader

private val dataset: State = State {
    when (it) {
        XMLStreamReader.START_ELEMENT ->
            if (reader.localName == "Series")
                current = series
    }
}

private val series: State = State {
    when (it) {
        XMLStreamReader.START_ELEMENT ->
            if (reader.localName == "SeriesKey") {
                rightValues = 0
                current = checkSeries
            }
    }
}

private val checkSeries: State = State {
    when (it) {
        XMLStreamReader.START_ELEMENT ->
            if (reader.localName == "Value") {
                if ((reader.getAttributeValue(0) == "s_OKATO") and
                    (reader.getAttributeValue(1) == "643")
                ) rightValues++
                if ((reader.getAttributeValue(0) == "s_OKVED2") and
                    (reader.getAttributeValue(1) == "62")
                ) rightValues++
                if (rightValues == 2) {
                    point = Point()
                    current = rightSeries
                }
            }
        XMLStreamReader.END_ELEMENT ->
            if (reader.localName == "SeriesKey")
                current = dataset
    }
}

private val rightSeries: State = State {
    when (it) {
        XMLStreamReader.START_ELEMENT -> {
            if (reader.localName == "Attributes")
                current = getAttributes
            if (reader.localName == "Obs")
                current = getObs
        }
        XMLStreamReader.END_ELEMENT ->
            if (reader.localName == "Series"){
                if(point.period!="")
                    result.add(point)
                current = dataset
            }
    }
}

private val getAttributes: State = State {
    when (it) {
        XMLStreamReader.START_ELEMENT ->
            if (reader.localName == "Value")
                if (reader.getAttributeValue(0) == "PERIOD") {
                    val period = reader.getAttributeValue(1)
                    if (!period.contains("-"))
                        point.period = period
                    current = rightSeries
                }
    }
}

private val getObs: State = State {
    when (it) {
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
                current = rightSeries
    }
}

private val result = ArrayList<Point>()
private var current: State = dataset
private lateinit var point:Point
private var rightValues = 0

fun main() {
    while (reader.hasNext())
        current.eval(reader.next())
    print(result)
}