package ch.dabsoft.dbweave

class GridData(var width: Int, var height: Int) {
    private var data = ArrayList<Int>(width * height);

    init {
        for (i in 0 until (width * height)) {
            data.add(0)
        }
    }

    operator fun get(x: Int, y: Int): Int {
        return data[x + width * y]
    }

    operator fun set(x: Int, y: Int, value: Int) {
        data[x + width * y] = value
    }

}
