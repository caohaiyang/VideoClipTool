package com.chy.clip.videocliptool

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import com.chy.clip.fileprovidersupport.FileProviderSupport
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

    private var mCurrentPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        findViewById<TextView>(R.id.tv4).setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePhotoIntent.resolveActivity(packageManager) != null) {
            val fileName = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                    .format(System.currentTimeMillis()) + ".png"
            val file = File(Environment.getExternalStorageDirectory(), fileName)
            val fileUri = FileProviderSupport.getFileUri(this, file)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            mCurrentPath = file.absolutePath
            val resolveInfoList = packageManager.queryIntentActivities(takePhotoIntent, PackageManager.MATCH_DEFAULT_ONLY)
            resolveInfoList
                    .map { it.activityInfo.packageName }
                    .forEach {
                        grantUriPermission(it, fileUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    }
            startActivityForResult(takePhotoIntent, REQUEST_CODE_TAKE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TAKE_PHOTO) {

            findViewById<ImageView>(R.id.img).setImageBitmap(BitmapFactory.decodeFile(mCurrentPath))
        }
    }

    companion object {
        private val REQUEST_CODE_TAKE_PHOTO = 0x1
    }
}
