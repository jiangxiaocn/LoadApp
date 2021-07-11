package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*



class DetailActivity : AppCompatActivity() {
    private var downLoadFileName = ""
    private var downLoadStatus = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        ok_button.setOnClickListener{
            val mainActivity=Intent(this,MainActivity::class.java)
            startActivity(mainActivity)
        }
        downLoadFileName = intent.getStringExtra("fileName").toString()
        downLoadStatus = intent.getStringExtra("status").toString()
        file_name.text = downLoadFileName
        status.text=downLoadStatus
    }

}
