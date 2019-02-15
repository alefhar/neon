package image

import java.io.File

class ImageWriter(private val fileName: String) {

    fun writePfm(image: Image) {
        val fileNameWithExtension = "$fileName.pfm"
        File(fileNameWithExtension).printWriter().use { out ->
            out.println("PF")
            out.println("${image.width} ${image.height}")
            out.println("-1.0")
        }

        val imageData = ByteArray(image.width * image.height * image.channels * Int.SIZE_BYTES)
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
        File(fileNameWithExtension).appendBytes(imageData)
    }

    fun writePpm(image: Image) {
        val fileNameWithExtension = "$fileName.ppm"
        File(fileNameWithExtension).printWriter().use { out ->
            out.println("P6")
            out.println("${image.width} ${image.height}")
            out.println("65535")
        }
        val imageData = ByteArray(image.width * image.height * image.channels * Short.SIZE_BYTES)
        var idx = 0
        for (y in 0 until image.width) {
            for (x in 0 until image.width) {
                for (c in 0 until image.channels) {
                    val subPixel = (image.get(x, y, c) * 65535).toInt()
                    val bytes = intToShortByteArray(subPixel)
                    for (i in 0 until bytes.size) {
                        imageData[idx] = bytes[i]
                        idx += 1
                    }
                }
            }
        }
        File(fileNameWithExtension).appendBytes(imageData)
    }
    
    private fun doubleToByteArray(d: Double): ByteArray {
        val bits = java.lang.Float.floatToIntBits(d.toFloat())
        val bytes = ByteArray(Int.SIZE_BYTES) { 0 }
        for (i in 0 until bytes.size) {
            val mask = 0xFF.shl(i * 8)
            bytes[i] = (bits and mask).shr(i * 8).toByte()
        }
        return bytes
    }

    private fun intToShortByteArray(s: Int): ByteArray {
        val bits = s
        val bytes = ByteArray(Short.SIZE_BYTES) { 0 }
        for (i in 0 until bytes.size) {
            val j = bytes.size - 1 - i
            val mask = 0xFF.shl(i * 8)
            bytes[j] = (bits and mask).shr(i * 8).toByte()
        }
        return bytes
    }
}