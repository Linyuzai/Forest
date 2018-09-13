package eason.linyuzai.forest.test

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.tbruyelle.rxpermissions2.RxPermissions
import eason.linyuzai.download.entity.DownloadTaskEntity
import eason.linyuzai.download.listeners.NetPerSecDownloadListener
import eason.linyuzai.download.listeners.NumberProgressDownloadListener
import eason.linyuzai.download.listeners.PercentProgressDownloadListener
import eason.linyuzai.download.listeners.RemainingTimeDownloadListener
import eason.linyuzai.download.task.DownloadTask
import eason.linyuzai.download.task.DownloadTaskWrapper
import eason.linyuzai.forest.R
import eason.linyuzai.forest.eload
import io.reactivex.disposables.Disposable


class DownloadTestActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DownloadTaskAdapter
    private val permissions = RxPermissions(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_download)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.rv_download_tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DownloadTaskAdapter()
        recyclerView.adapter = adapter
        eload().loadTaskEntitiesFromDatabase().forEach { adapter.add(it) }
        /*findViewById<View>(R.id.start).setOnClickListener {
            val permissions = RxPermissions(this)
            permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).filter { it }.subscribe {
                //startWithPermissions()
            }
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_test_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.add -> {
                permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).filter { it }.subscribe {
                    //http://img.zcool.cn/community/0117e2571b8b246ac72538120dd8a4.jpg@1280w_1l_2o_100sh.jpg
                    //http://192.168.11.139:8080/download
                    adapter.add(eload().url("http://192.168.5.107:8080/download")
                            .filename("${adapter.itemCount}.apk")
                            .urlDecoder("utf-8").entity())
                    recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                }
                return true
            }
            R.id.del -> {
                eload().databaseManager.deleteAll()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    data class ELoadTask(val entity: DownloadTaskEntity, val task: DownloadTask,
                         val np: NumberProgressDownloadListener,
                         val pp: PercentProgressDownloadListener,
                         val nps: NetPerSecDownloadListener,
                         val rt: RemainingTimeDownloadListener,
                         var progress: Int = 0,
                         var number: String = "",
                         var percent: String = "",
                         var speed: String = "",
                         var time: String = "",
                         var disposable: Disposable? = null)

    inner class DownloadTaskAdapter : RecyclerView.Adapter<DownloadTaskViewHolder>() {

        private val tasks = arrayListOf<ELoadTask>()

        fun add(entity: DownloadTaskEntity) {
            Log.d("add", entity.toString())
            var eTask: ELoadTask? = null
            val np = object : NumberProgressDownloadListener(null, null) {
                override fun updateProgress(progressBar: ProgressBar?, progress: Int) {
                    //Log.d("np", progress.toString())
                    eTask?.progress = progress
                    super.updateProgress(progressBar, progress)
                }

                override fun updateText(textView: TextView?, text: String?) {
                    //Log.d("np", text.toString())
                    eTask?.number = text ?: ""
                    super.updateText(textView, text)
                }
            }
            val pp = object : PercentProgressDownloadListener(null, null) {
                override fun updateText(textView: TextView?, text: String?) {
                    //Log.d("pp", text.toString())
                    eTask?.percent = text ?: ""
                    super.updateText(textView, text)
                }
            }
            val nps = object : NetPerSecDownloadListener(null) {
                override fun updateText(textView: TextView?, text: String?) {
                    //Log.d("nps", text.toString())
                    eTask?.speed = text ?: ""
                    super.updateText(textView, text)
                }
            }
            val rt = object : RemainingTimeDownloadListener(null) {
                override fun updateText(textView: TextView?, text: String?) {
                    //Log.d("rt", text.toString())
                    eTask?.time = text ?: ""
                    super.updateText(textView, text)
                }
            }
            val task = eload().convert(entity)
                    .downloadListener(np)
                    .downloadListener(pp)
                    .downloadListener(nps)
                    .downloadListener(rt)
                    /*.downloadTaskListener(object : AbsDownloadTaskListener() {
                        override fun onDownloadTaskError(task: DownloadTask?, e: Throwable?) {
                            e?.printStackTrace()
                        }
                    })*/
                    .create()
            eTask = ELoadTask(entity, task, np, pp, nps, rt)
            //Log.d("after", )
            np.onProgress(null, null, entity.totalBytes, entity.downloadBytes,
                    entity.downloadBytes * 1.0 / entity.totalBytes * 100)
            pp.onProgress(null, null, entity.totalBytes, entity.downloadBytes,
                    entity.downloadBytes * 1.0 / entity.totalBytes * 100)
            nps.onInterval(0)
            rt.onInterval(0)
            tasks.add(eTask)
            notifyItemInserted(tasks.size - 1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadTaskViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_test_download_item, parent, false)
            return DownloadTaskViewHolder(view)
        }

        override fun getItemCount(): Int = tasks.size

        override fun onBindViewHolder(holder: DownloadTaskViewHolder, position: Int) {
            val task = tasks[position]
            holder.reset(task)
            if (task.disposable == null) {
                task.disposable = task.task.toTaskObservable().subscribe({
                    if (it.callType in arrayOf(DownloadTaskWrapper.TASK_PAUSE, DownloadTaskWrapper.TASK_COMPLETE,
                                    DownloadTaskWrapper.TASK_ERROR, DownloadTaskWrapper.TASK_RESUME))
                        notifyItemChanged(position)
                }, Throwable::printStackTrace)
            }
            if (task.task.isAttach)
                task.task.start()
            when {
                task.task.isRunning -> {
                    holder.itemView.setBackgroundColor(Color.parseColor("#B0E2FF"))
                    holder.resume.visibility = View.GONE
                    holder.pause.visibility = View.VISIBLE
                }
                task.task.isPause -> {
                    holder.itemView.setBackgroundColor(Color.parseColor("#EEE685"))
                    holder.resume.visibility = View.VISIBLE
                    holder.pause.visibility = View.GONE
                }
                task.task.isFinish -> {
                    holder.itemView.setBackgroundColor(Color.parseColor("#90EE90"))
                    holder.resume.visibility = View.INVISIBLE
                    holder.pause.visibility = View.GONE
                }
                task.task.isError -> {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFAEB9"))
                    holder.resume.visibility = View.VISIBLE
                    holder.pause.visibility = View.GONE
                }
            }
            if (task.task.isFinish) {
                task.task.recycle()
                task.np.progressBar = null
                task.np.textView = null
                task.pp.textView = null
                task.nps.textView = null
                task.rt.textView = null
                holder.pause.setOnClickListener(null)
                holder.resume.setOnClickListener(null)
            } else {
                task.np.progressBar = holder.progress
                task.np.textView = holder.number
                task.pp.textView = holder.percent
                task.nps.textView = holder.speed
                task.rt.textView = holder.time
                holder.pause.setOnClickListener {
                    task.task.pause()
                }
                holder.resume.setOnClickListener {
                    task.task.resume()
                }
            }
        }
    }

    class DownloadTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val path = view.findViewById<TextView>(R.id.path)!!
        val progress = view.findViewById<ProgressBar>(R.id.progress)!!
        val number = view.findViewById<TextView>(R.id.number)!!
        val percent = view.findViewById<TextView>(R.id.percent)!!
        val speed = view.findViewById<TextView>(R.id.speed)!!
        val time = view.findViewById<TextView>(R.id.time)!!
        val pause = view.findViewById<Button>(R.id.pause)!!
        val resume = view.findViewById<Button>(R.id.resume)!!

        fun reset(task: ELoadTask) {
            path.text = "${task.task.entity.filePath}/${task.task.entity.fileName}"
            progress.progress = task.progress
            number.text = task.number
            percent.text = task.percent
            speed.text = task.speed
            time.text = task.time
        }
    }
}