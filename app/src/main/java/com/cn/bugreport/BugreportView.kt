package com.cn.bugreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cn.bugreport.models.Bugreport
import com.cn.bugreport.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_bugreport_view.*
import kotlinx.android.synthetic.main.bugreport_row.view.*

class BugreportView : AppCompatActivity() {
    var bugreporter: User? = null
    var bugreport: Bugreport? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bugreport_view)

        Thread.setDefaultUncaughtExceptionHandler(TopExceptionHandler(this))

        bugreporter = intent.getParcelableExtra<User>(MainActivity.USER_KEY_BUGREPORTER)
        bugreport = intent.getParcelableExtra<Bugreport>(MainActivity.USER_KEY_BUGREPORT)

        Picasso.get().load(bugreporter?.profileImageUrl).into(imageView_bugreport_view)
        username_textview_bugreport_view.text = bugreporter?.username
        supportActionBar?.title = bugreport?.title
        content_textview_bugreport_view.text = bugreport?.content

    }
}