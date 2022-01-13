package kr.co.prnd.scopedstoragesample

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.launch
import kr.co.prnd.scopedstoragesample.bitmap.HeyDealerBitmapSaver
import kr.co.prnd.scopedstoragesample.databinding.ActivityMainBinding
import kr.co.prnd.scopedstoragesample.download.HeyDealerDownloader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var heyDealerBitmapSaver: HeyDealerBitmapSaver
    private lateinit var heyDealerDownloader: HeyDealerDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        heyDealerBitmapSaver = HeyDealerBitmapSaver.newInstance(this)
        heyDealerDownloader = HeyDealerDownloader.newInstance(this)

        binding.btnSaveImage.setOnClickListener {
            saveImage("https://picsum.photos/200")
        }
        binding.btnSaveTextFile.setOnClickListener {
            downloadFile("https://raw.githubusercontent.com/PRNDcompany/android-style-guide/master/README.md")
        }
    }

    private fun saveImage(imageUrl: String) {
        Glide.with(this@MainActivity)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    lifecycleScope.launch {
                        val fileName = System.currentTimeMillis().toString()
                        val isSuccess = heyDealerBitmapSaver.save(resource, fileName).getOrElse { false }
                        if (isSuccess) {
                            Toast.makeText(this@MainActivity, "이미지 저장 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "이미지 저장 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    /* no-op */
                }
            })
    }

    private fun downloadFile(downloadUrl: String) {
        lifecycleScope.launch {
            heyDealerDownloader.startDownloadFlow(downloadUrl) {
                Toast.makeText(this@MainActivity, "다운로드 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
