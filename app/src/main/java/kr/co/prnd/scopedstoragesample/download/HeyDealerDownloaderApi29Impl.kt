package kr.co.prnd.scopedstoragesample.download

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(value = Build.VERSION_CODES.Q)
internal class HeyDealerDownloaderApi29Impl(
    context: Context
) : BaseHeyDealerDownloader(context) {
    // API 29 이상에서는 권한이 필요없음
    override suspend fun isStoragePermissionGranted(): Boolean = true
}
