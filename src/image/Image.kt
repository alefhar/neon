package image

class Image(val width: Int, val height: Int, val channels: Int) {

    private val pixels = DoubleArray(width * height * channels) { 0.0 }

    fun set(x: Int, y: Int, c: Int, subPixel: Double) {
        pixels[stride(x, y, c)] = subPixel
    }

    fun get(x: Int, y: Int, c: Int): Double {
        return pixels[stride(x, y, c)]
    }

    private fun stride(x: Int, y: Int, c: Int): Int {
        return c + channels * (y * width + x)
    }
}