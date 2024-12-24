package com.example.bookinventoryapp.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.common.CharacterSetECI

fun generateQRCode(data: String): Bitmap? {
    val writer = QRCodeWriter()
    return try {
        val hintMap = mapOf<com.google.zxing.EncodeHintType, Any>(
            com.google.zxing.EncodeHintType.CHARACTER_SET to CharacterSetECI.UTF8.name
        )

        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512, hintMap)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bmp
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }
}
