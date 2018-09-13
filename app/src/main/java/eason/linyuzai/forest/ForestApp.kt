package eason.linyuzai.forest

import android.app.Application
import android.os.Environment
import com.tencent.bugly.Bugly
import eason.linyuzai.download.ELoad
import eason.linyuzai.download.database.SQLiteManager
import java.io.File

class ForestApp : Application() {

    private lateinit var eload: ELoad

    override fun onCreate() {
        super.onCreate()
        eload = ELoad.Builder(this)
                .setDownloadPath(File(Environment.getExternalStorageDirectory(), "Forest").absolutePath)
                //.setDatabaseManager(XUtilsDatabaseManager(this))
                .setDatabaseManager(SQLiteManager(this))
                .build()
        Bugly.init(this, "e7067bfeb2", false)
        //CrashReport.testJavaCrash()
    }

    fun eload() = eload
}
