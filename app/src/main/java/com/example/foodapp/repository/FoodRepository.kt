package com.example.foodapp.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Food
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.IOException

class FoodRepository {
    private var foodListData: MutableLiveData<List<Food>> = MutableLiveData(ArrayList())
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var foodList: List<Food> = ArrayList()

    companion object {
        private var INSTANCE: FoodRepository? = null
        private lateinit var storageRef: StorageReference
        fun getInstance(): FoodRepository {
            if (INSTANCE == null) {
                INSTANCE = FoodRepository()
                storageRef = FirebaseStorage.getInstance().reference
            }
            return INSTANCE!!
        }
    }

    //read json file from assets folder
    private fun getJSONData(context: Context): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open("data.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            Log.e("TAG", "getJSONData: ${ioException.printStackTrace()}")
            return null
        }
        return jsonString
    }

    //returns food list data from json file
    fun getFoodList(context: Context): MutableLiveData<List<Food>> {
        updateLoadingStatus()
        val jsonString = getJSONData(context)

        val gson = Gson()
        val foodListType = object : TypeToken<List<Food>>() {}.type

        foodList = gson.fromJson(jsonString, foodListType)
        foodList.forEachIndexed { index, item ->
            item.imageBitmap = createBitmapFromString(index)
            doFirebaseWork(index)

            if (index == foodList.size.minus(1)) {
                foodListData.postValue(foodList)
            }
        }
        return foodListData
    }

    //creates a bitmap from the image string to be uploaded to firebase
    private fun createBitmapFromString(index: Int): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 30.0F
        paint.textAlign = Paint.Align.CENTER
        val width = 80
        val height = 80
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawColor(Color.WHITE)
        canvas.drawText(foodList[index].image, 40F, 50F, paint)

        return image
    }

    private fun doFirebaseWork(index: Int) {
        storageRef.child("${foodList[index].productName}.jpg").downloadUrl.addOnSuccessListener {
            //image already uploaded
            foodList[index].imageUrl = it.toString()
            foodListData.postValue(foodList)

            updateLoadingStatus(index)
        }.addOnFailureListener {
            // image not uploaded then upload image
            uploadImage(index)
        }
    }

    private fun uploadImage(index: Int) {
        val imgRef = storageRef.child("${foodList[index].productName}.jpg")

        val baos = ByteArrayOutputStream()
        foodList[index].imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val data = baos.toByteArray()
        val uploadTask = imgRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener {
                foodList[index].imageUrl = it.toString()
                foodListData.postValue(foodList)

                updateLoadingStatus(index)
            }.addOnFailureListener {
                Log.d("TAG", "uploadImage: What went wrong urlTask: ${it.localizedMessage}")
                updateLoadingStatus(index)
            }

        }.addOnFailureListener {
            Log.d("TAG", "uploadImage: What went wrong ${it.localizedMessage}")
            if (index == foodList.size - 1) {
                isLoading.postValue(false)
            }
        }
    }

    private fun updateLoadingStatus(index: Int) {
        if (index == foodList.size - 1) {
            isLoading.postValue(false)
        }
    }

    private fun updateLoadingStatus() {
        isLoading.postValue(true)
    }
}