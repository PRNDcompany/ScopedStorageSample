package kr.co.prnd.scopedstoragesample.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity

internal abstract class BaseHeyDealerDownloader(private val context: Context) :
    HeyDealerDownloader {
    private val downloadManager by lazy {
        context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
    }

    protected abstract suspend fun isStoragePermissionGranted(): Boolean

    override suspend fun startDownloadFlow(
        downloadUrl: String,
        onDownloadComplete: (() -> Unit)?,
    ) {
        if (!isStoragePermissionGranted()) return
        if (onDownloadComplete != null) {
            addDownloadCompleteListener(onDownloadComplete)
        }

        downloadFile(downloadUrl)
    }

    private fun downloadFile(downloadUrl: String) {
        val uri = Uri.parse(downloadUrl)
        val fileName = uri.lastPathSegment
        val request = DownloadManager.Request(uri)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadManager.enqueue(request)
    }

    private fun addDownloadCompleteListener(listener: DownloadCompleteListener) {
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                listener.onDownloadComplete()
                context.unregisterReceiver(this)
            }
        }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    protected fun interface DownloadCompleteListener {
        fun onDownloadComplete()
    }
}

