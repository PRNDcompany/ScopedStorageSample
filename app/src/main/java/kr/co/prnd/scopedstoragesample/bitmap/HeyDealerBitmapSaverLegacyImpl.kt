package kr.co.prnd.scopedstoragesample.bitmap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import com.gun0912.tedpermission.coroutine.TedPermission
import java.io.File
import java.io.FileOutputStream

internal class HeyDealerBitmapSaverLegacyImpl(
    private val context: Context
) : BaseHeyDealerBitmapSaver() {
    override suspend fun save(bitmap: Bitmap, fileName: String): Result<Boolean> {
        if (!requestPermission()) {
            // 사용자가 권한을 거부하는 상황은 예외적인 상황은 아니기 때문에
            // 성공을 반환하고 저장은 실패했음을 알 수 있도록 false를 반환
            return Result.success(false)
        }

        return runCatching {
            @Suppress("DEPRECATION")
            val pictureFolder = Environment.getExternalStorageDirectory()
                .toString() + "/" + Environment.DIRECTORY_PICTURES
            val savePath = "$pictureFolder/heydealer"
            val imageFolder = File(savePath).apply {
                if (!isDirectory) mkdirs()
            }
            val completeFileName = "/$fileName.jpg"
            FileOutputStream(savePath + completeFileName).saveBitmap(bitmap)
            scanPhoto(context, imageFolder.absolutePath + completeFileName)
            return@runCatching true
        }
    }

    private fun scanPhoto(context: Context, imagePath: String) {
        @Suppress("DEPRECATION")
        val intent = Intent(
            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
            Uri.parse("file://$imagePath")
        )
        context.sendBroadcast(intent)
    }

    private suspend fun requestPermission(): Boolean =
        TedPermission.create()
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .checkGranted()
}
