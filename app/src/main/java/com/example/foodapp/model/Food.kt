package com.example.foodapp.model

import android.graphics.Bitmap
import java.io.Serializable

data class Food(
    val description: String,
    val from: String,
    val id: Int,
    val image: String,
    val nutrients: String,
    val organic: Boolean,
    val price: String,
    val productName: String,
    val quantity: String,
    var imageBitmap: Bitmap,
    var imageUrl: String,
): Serializable