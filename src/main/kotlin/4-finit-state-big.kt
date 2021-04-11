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
            if (reader.localName == "SeriesKey")
                current = checkSeries
        XMLStreamReader.END_ELEMENT ->
            if (reader.localName == "Series")
                current = dataset
    }
}

private val checkSeries: State = State {
    when (it) {
    }
}


private val getAttributes: State = State {
    when (it) {
    }
}

private val getPeriod: State = State {
    when (it) {
    }
}

private val getObs: State = State {
    when (it) {
    }
}

private val getTine: State = State {
    when (it) {
    }
}

private val getValue: State = State {
    when (it) {
    }
}

private val result = ArrayList<Point>()
private var current: State = dataset

fun main() {
    while (reader.hasNext())
        current.eval(reader.next())
    print(result)
}