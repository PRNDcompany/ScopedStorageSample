package kr.co.prnd.scopedstoragesample.bitmap

import android.graphics.Bitmap
import java.io.OutputStream

internal abstract class BaseHeyDealerBitmapSaver : HeyDealerBitmapSaver {
    protected fun OutputStream.saveBitmap(bitmap: Bitmap) = use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, this)
        flush()
    }
}
