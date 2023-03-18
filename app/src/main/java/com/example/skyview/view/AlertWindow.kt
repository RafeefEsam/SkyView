package com.example.skyview.view

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.skyview.R

class AlertWindow(private val context: Context) {

    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var view: View
    private var isShown = false


    fun show(title: String, message: String) {
        if (isShown) return

        // Initialize the window manager and layout params
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams().apply {
            type =
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
            y = 0
        }

        // Create the view and set its contents
        view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
        view.findViewById<TextView>(R.id.dialogSubTitle).text = title
        view.findViewById<TextView>(R.id.dialogMsg).text = message
        view.findViewById<ImageView>(R.id.dialogCloseBtn).setOnClickListener{
            hide()
        }
        view.findViewById<Button>(R.id.dismissAlertBtn).setOnClickListener {
            hide()
        }

        // Add the view to the window manager and show it
        windowManager.addView(view, layoutParams)
        isShown = true

        val mediaPlayer = MediaPlayer.create(context, R.raw.noti)
        mediaPlayer.setOnCompletionListener {
            it.start()
          //  it.stop()
            it.release()
        }
        mediaPlayer.start()
    }

    private fun hide() {
        if (!isShown) return
        windowManager.removeView(view)
        isShown = false
    }
}
