package kr.co.prnd.scopedstoragesample.download

import android.content.Context
import android.os.Build

interface HeyDealerDownloader {

    suspend fun startDownloadFlow(
        downloadUrl: String,
        onDownloadComplete: (() -> Unit)? = null
    )

    companion object {
        fun newInstance(context: Context): HeyDealerDownloader {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                HeyDealerDownloaderApi29Impl(context)
            } else {
                HeyDealerDownloaderLegacyImpl(context)
            }
        }
    }
}
