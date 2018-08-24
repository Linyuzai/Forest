package eason.linyuzai.forest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import eason.linyuzai.forest.test.DownloadTestActivity

class ForestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forest)
        findViewById<View>(R.id.download_test).setOnClickListener {
            startActivity(Intent(this, DownloadTestActivity::class.java))
        }
    }
}
