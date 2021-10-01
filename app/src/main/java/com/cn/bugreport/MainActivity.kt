package com.cn.bugreport

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.cn.bugreport.models.Bugreport
import com.cn.bugreport.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "MainActivity"
        val USER_KEY_BUGREPORT = "BUGREPORT"
        val USER_KEY_BUGREPORTER = "BUGREPORTER"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //각 액티비티별 onCreate마다 설정을 해서 에러 핸들링 진행(On-going)
        Thread.setDefaultUncaughtExceptionHandler(TopExceptionHandler(this))

        verifyUserIsLoggedIn()

        bugreport_button_main.setOnClickListener {
            val intent = Intent(this, BugreportActivity::class.java)
            startActivity(intent)
        }

        recyclerview_main.adapter = adapter
        recyclerview_main.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, BugreportView::class.java)
            val row = item as BugreportRow
            intent.putExtra(USER_KEY_BUGREPORT, row.bugreport)
            intent.putExtra(USER_KEY_BUGREPORTER, row.bugreporter)
            startActivity(intent)
        }

        listenBugreport()

        raise_exception_button_main.setOnClickListener {
            throw Exception("Test for stacktrace")
        }
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    val adapter = GroupieAdapter()

    private fun listenBugreport() {
        val ref = FirebaseDatabase.getInstance().getReference("/bugreports")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val bugreport = snapshot.getValue(Bugreport::class.java) ?: return
                adapter.add(BugreportRow(bugreport))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(TAG, "onChildChanged")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.i(TAG, "onChildRemoved")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(TAG, "onChildMoved")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, "onCancelled")
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}