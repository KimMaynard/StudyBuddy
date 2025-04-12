package com.example.studybuddybackend.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.common.BitMatrix
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

object QrCodeGenUtil {
    private const val QR_CODE_WIDTH = 250
    private const val QR_CODE_HEIGHT = 250

    /*
     * Generates a QR code image (as binary data) from the joinUrl (which the qrcode stores).
     * Returns a Pair where first is the join URL and second is the generated
     * QR code image data as a ByteArray
    */
    fun generateQRCode(joinUrl: String): Pair<String, ByteArray> {
        // Hints for encoding.
        val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

        // Generates QR code bit matrix
        val bitMatrix: BitMatrix = QRCodeWriter().encode(joinUrl, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT, hints)

        // Creates QR code buffered image
        val image = BufferedImage(QR_CODE_WIDTH, QR_CODE_HEIGHT, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until QR_CODE_WIDTH) {
            for (y in 0 until QR_CODE_HEIGHT) {
                val grayValue = if (bitMatrix.get(x, y)) 0x000000 else 0xFFFFFF
                image.setRGB(x, y, grayValue)
            }
        }

        // Converts image to a ByteArrayOutputStream
        val baos = ByteArrayOutputStream()
        ImageIO.write(image, "png", baos)
        val imageData = baos.toByteArray()

        // Returns joinUrl with the QR code image data
        return Pair(joinUrl, imageData)
    }
}
