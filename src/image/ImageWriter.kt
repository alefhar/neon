package image

import java.io.File

class ImageWriter(private val fileName: String) {

    fun writePfm(image: Image) {
        File(fileName).printWriter().use { out ->
            out.println("PF")
            out.println("${image.width} ${image.height}")
            out.println("-1.0")
        }

        val imageData = ByteArray(image.sizeInBytes()) { 0 }
        var idx = 0
        for (y in 0 until image.width) {
            for (x in 0 until image.width) {
                for (c in 0 until image.channels) {
                    val subPixel = image.get(x, y, c)
                    val bytes = doubleToByteArray(subPixel)
                    for (i in 0 until bytes.size) {
                        imageData[idx] = bytes[i]
                        idx += 1
                    }
                }
            }
        }
        File(fileName).appendBytes(imageData)
    }

    private fun doubleToByteArray(d: Double): ByteArray {
        val bits = java.lang.Float.floatToIntBits(d.toFloat())
        val bytes = ByteArray(Int.SIZE_BYTES) { 0 }
        for (i in 0 until Int.SIZE_BYTES) {
            val mask = 0xFF.shl(i * 8)
            bytes[i] = (bits and mask).shr(i * 8).toByte()
        }
        return bytes
    }
}