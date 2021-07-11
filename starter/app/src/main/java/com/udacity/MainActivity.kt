package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_detail.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var selectedFileName :String ?= null
    lateinit var loadingButton: LoadingButton
    private var URL : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //set filter to only when download is complete and register broadcast receiver
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loadingButton = findViewById(R.id.custom_button)
        loadingButton.setLoadingButtonState(ButtonState.Completed)
        notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        custom_button.setOnClickListener {
            download()
        }
    }
    fun radioButtonSelectFileName(view: View){
        if (view is RadioButton) {
            val ischecked = view.isChecked
            when (view.getId()) {
                R.id.glide_button ->
                    if (ischecked) {
                        selectedFileName = getString(R.string.glide)
                        URL = "https://github.com/bumptech/glide"
                    }
                R.id.load_app_button ->
                    if (ischecked) {
                        selectedFileName = getString(R.string.load_app)
                        URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                    }
                R.id.retrofit_button ->
                    if (ischecked) {
                        selectedFileName = getString(R.string.retrofit)
                        URL = "https://github.com/square/retrofit"
                    }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val action = intent?.action

           if (downloadID==id) {
               if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                   val query = DownloadManager.Query()
                   query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
                   //Query the download manager about downloads that have been requested.
                   val cursor: Cursor = downloadManager.query(query)
                   if(cursor.moveToFirst()){
                       //Current status of the download
                       val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                       val status = cursor.getInt(columnIndex)
                       if (status == DownloadManager.STATUS_SUCCESSFUL) {
                           loadingButton.setLoadingButtonState(ButtonState.Completed)
                           notificationManager.sendNotification(selectedFileName.toString(), applicationContext, "Success")
                       }else{
                           loadingButton.setLoadingButtonState(ButtonState.Completed)
                           notificationManager.sendNotification(selectedFileName.toString(), applicationContext, "Fail")
                       }

                       }
                   }
               }
        }
    }



    private fun download() {
        if (URL != null) {
            loadingButton.setLoadingButtonState(ButtonState.Loading)
            createChannel(getString(R.string.notification_channel_id), getString(R.string.notification_channel_name))
            val request =
                    DownloadManager.Request(Uri.parse(URL))
                            .setTitle(getString(R.string.app_name))
                            .setDescription(getString(R.string.app_description))
                            .setRequiresCharging(false)
                            .setAllowedOverMetered(true)
                            //Set whether this download may proceed over a roaming connection.
                            .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                    downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }else{
            Toast.makeText(this, "please select a file to download", Toast.LENGTH_SHORT).show()
        }
    }

    /*companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }*/

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    // TODO: Step 2.4 change importance
                    NotificationManager.IMPORTANCE_HIGH
            )
                    // TODO: Step 2.6 disable badges for this channel
                    .apply {
                        setShowBadge(false)
                    }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download is done!"
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

}
