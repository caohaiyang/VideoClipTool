package com.chy.clip.fileprovidersupport

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import java.io.File

class FileProviderSupport {
    companion object {

        fun getFileUri(context : Context, file : File): Uri? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getFileUri24(context, file)
            } else {
                Uri.fromFile(file)
            }
        }

        fun getFileUri24(context : Context, file : File): Uri? {
            val uri = FileProvider.getUriForFile(context, "com.chy.clip.fileprovider", file);

            return uri
        }

    }
}
