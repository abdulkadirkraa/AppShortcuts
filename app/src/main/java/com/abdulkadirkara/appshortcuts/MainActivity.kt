package com.abdulkadirkara.appshortcuts

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    lateinit var text : TextView
    lateinit var button : Button

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        handleIntent(intent)

        text = findViewById(R.id.text)
        button = findViewById(R.id.button)

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

}