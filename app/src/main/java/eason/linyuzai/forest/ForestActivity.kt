package eason.linyuzai.forest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import eason.linyuzai.easonbar.nav.EasonNav
import eason.linyuzai.easonbar.tab.EasonTab
import eason.linyuzai.elib.component.EasonActivity
import eason.linyuzai.forest.test.DownloadTestActivity
import eason.linyuzai.forest.test.LayoutManagerTestActivity

class ForestActivity : EasonActivity() {

    private lateinit var nav: EasonNav
    private lateinit var tab: EasonTab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val mainView = EasonMainView(this)
        mainView.initialize()
        nav = mainView.easonNav
        tab = mainView.easonTab
        setContentView(mainView)*/
        setContentView(R.layout.activity_forest)
        id<View>(R.id.download_test).setOnClickListener {
            startActivity(Intent(this, DownloadTestActivity::class.java))
        }
        id<View>(R.id.layout_manager_test).setOnClickListener {
            startActivity(Intent(this, LayoutManagerTestActivity::class.java))
        }
        val v: View = id(R.id.view)
        id<Button>(R.id.scale).setOnClickListener {
            v.pivotY = 0f
            v.rotationX -= 10
        }
    }
}
