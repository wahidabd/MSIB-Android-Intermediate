package com.wahidabd.dicodingstories.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.snackbar.Snackbar
import com.wahidabd.dicodingstories.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun EditText.texTrim() = this.text.toString().trim()

fun View.mySnackBar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.setImage(url: String) =
    Glide.with(this)
        .load(url)
        .into(this)


@SuppressLint("SimpleDateFormat", "SetTextI18n")
fun TextView.convertDate(date: String, context: Context){

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val pasTime = dateFormat.parse(date)
    val nowTime = Date()

    val dateDiff = nowTime.time - (pasTime?.time ?: 0)

    val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
    val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
    val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
    val day = TimeUnit.MILLISECONDS.toDays(dateDiff)

    if (second < 60) this.text = "$second ${context.getString(R.string.date_second)}"
    else if (minute < 60) this.text = "$minute ${context.getString(R.string.date_minute)}"
    else if (hour < 24) this.text = "$hour ${context.getString(R.string.date_hour)}"
    else if (day >= 7){
        if (day > 360) this.text = "${day/360} ${context.getString(R.string.date_day)}"
        else if (day > 30) this.text = "${day/30} ${context.getString(R.string.date_month)}"
        else this.text = "${day/7} ${context.getString(R.string.date_week)}"
    }else if (day < 7) this.text = "$day ${context.getString(R.string.date_day)}"
}

val timeStamp: String = SimpleDateFormat(
    "dd-MMM-yyyy",
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun bitmapFromVector(context: Context, resId: Int): BitmapDescriptor? =
    ContextCompat.getDrawable(context, resId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
