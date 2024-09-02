package com.abdulkadirkara.appshortcuts

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    lateinit var text : TextView
    lateinit var button : Button
    lateinit var button2 : Button

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        handleIntent(intent)

        text = findViewById(R.id.text)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)

        viewModel.shortcutType.observe(this) { type ->
            when (type) {
                ShortcutType.STATIC -> text.text = "Static shortcut clicked"
                ShortcutType.DYNAMIC -> text.text = "Dynamic shortcut clicked"
                ShortcutType.PINNED -> text.text = "Pinned shortcut clicked"
                null -> Unit
            }
        }
        button.setOnClickListener{
            addDynamicShortCut()
        }
        button2.setOnClickListener{
            addPinnedShortcut()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun addDynamicShortCut(){
        val shortcut = ShortcutInfoCompat.Builder(this, "id")
            .setShortLabel("Call Mum")
            .setLongLabel("Clicking this will call your mum")
            .setIcon(IconCompat.createWithResource(this, R.drawable.baseline_call_24))
            .setIntent(
                Intent(this, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("shortcut_id", "dynamic")
                })
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)

    }

    private fun handleIntent(intent: Intent?){
        intent?.let {
            when(intent.getStringExtra("shortcut_id")){
                "static" -> viewModel.setShortcutType(ShortcutType.STATIC)
                "dynamic" -> viewModel.setShortcutType(ShortcutType.DYNAMIC)
                "pinned" -> viewModel.setShortcutType(ShortcutType.PINNED)
            }
        }
    }

    private fun addPinnedShortcut() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val shortcutManager = getSystemService<ShortcutManager>()!!
        if(shortcutManager.isRequestPinShortcutSupported) {
            val shortcut = ShortcutInfo.Builder(applicationContext, "pinned")
                .setShortLabel("Send message")
                .setLongLabel("This sends a message to a friend")
                .setIcon(
                    Icon.createWithResource(
                    applicationContext, R.drawable.baseline_push_pin_24
                ))
                .setIntent(
                    Intent(applicationContext, MainActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                        putExtra("shortcut_id", "pinned")
                    }
                )
                .build()

            val callbackIntent = shortcutManager.createShortcutResultIntent(shortcut)
            val successPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                callbackIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            shortcutManager.requestPinShortcut(shortcut, successPendingIntent.intentSender)
        }
    }

}