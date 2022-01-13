package kr.co.prnd.scopedstoragesample.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.contentValuesOf

@RequiresApi(value = Build.VERSION_CODES.Q)
internal class HeyDealerBitmapSaverApi29Impl(private val context: Context) : BaseHeyDealerBitmapSaver() {
    override suspend fun save(bitmap: Bitmap, fileName: String): Result<Boolean> {
        return try {
            val contentValues = contentValuesOf(
                MediaStore.MediaColumns.DISPLAY_NAME to "$fileName.jpg",
                MediaStore.MediaColumns.MIME_TYPE to "image/jpeg",
                MediaStore.MediaColumns.RELATIVE_PATH to "${Environment.DIRECTORY_PICTURES}/heydealer",
            )

            val contentResolver = context.contentResolver
            val imageUri = contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: return Result.failure(IllegalStateException("Failed insert contentValues. contentValues: $contentValues"))
            val outputStream = contentResolver.openOutputStream(imageUri)
                ?: return Result.failure(IllegalStateException("Failed openOutputStream. imageUri: $imageUri, contentValues: $contentValues"))
            outputStream.saveBitmap(bitmap)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
