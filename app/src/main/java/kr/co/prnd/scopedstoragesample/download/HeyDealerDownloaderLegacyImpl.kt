package kr.co.prnd.scopedstoragesample.download

import android.Manifest
import android.content.Context
import com.gun0912.tedpermission.coroutine.TedPermission

internal class HeyDealerDownloaderLegacyImpl(
    context: Context
) : BaseHeyDealerDownloader(context) {
    override suspend fun isStoragePermissionGranted(): Boolean =
        TedPermission.create()
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .checkGranted()
}
