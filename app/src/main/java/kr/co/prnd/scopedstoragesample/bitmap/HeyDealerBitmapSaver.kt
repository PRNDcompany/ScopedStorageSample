package kr.co.prnd.scopedstoragesample.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.os.Build

interface HeyDealerBitmapSaver {
    suspend fun save(bitmap: Bitmap, fileName: String): Result<Boolean>

    companion object {
        internal fun newInstance(context: Context): HeyDealerBitmapSaver =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                HeyDealerBitmapSaverApi29Impl(context)
            } else {
                HeyDealerBitmapSaverLegacyImpl(context)
            }
    }
}
