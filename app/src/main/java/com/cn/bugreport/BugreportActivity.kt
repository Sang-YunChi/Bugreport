package com.cn.bugreport

import android.content.ContextWrapper
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cn.bugreport.models.Bugreport
import com.cn.bugreport.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_bugreport.*
import java.io.File
import java.util.*

class BugreportActivity : AppCompatActivity() {

    companion object {
        val TAG = "BugreportActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bugreport)

        Thread.setDefaultUncaughtExceptionHandler(TopExceptionHandler(this))

        supportActionBar?.title = "Add Bugreport"
        save_button_bugreport.setOnClickListener {
            val reporterId = FirebaseAuth.getInstance().uid
            val title = title_edittext_bugreport.text.toString()
            val content = content_edittext_bugreport.text.toString()

            if (title.isEmpty() || content.isEmpty()) return@setOnClickListener

            val contextWrapper = ContextWrapper(this)
            val stack_trace_file_path = "${contextWrapper.filesDir.toString()}/stack.trace"

            val filename = UUID.randomUUID().toString()
            val storageRef = FirebaseStorage.getInstance().getReference("/bugreports/$filename")

            storageRef.putFile(Uri.fromFile(File(stack_trace_file_path)))
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully uploaded stack trace: ${it.metadata?.path}")

                    storageRef.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "File Location: $it")

                        val reference = FirebaseDatabase.getInstance().getReference("/bugreports").push()

                        val bugreport = Bugreport(reference.key!!, reporterId!!, it.toString(), title, content, System.currentTimeMillis() / 1000)
                        reference.setValue(bugreport)
                            .addOnSuccessListener {
                                Log.d(TAG, "Saved our bugreport: ${reference.key}")
                                finish()
                            }
                    }
                }
                .addOnFailureListener {
                    Log.d(RegisterActivity.TAG, "Failed to upload image to storage: ${it.message}")
                }
        }
    }
}