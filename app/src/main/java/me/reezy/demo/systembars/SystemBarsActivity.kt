package me.reezy.demo.systembars

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_system_bars.*
import me.reezy.jetpack.systembars.*

class SystemBarsActivity : AppCompatActivity() {

    val colors by lazy { arrayOf(window.statusBarColor, 0x88ff0000.toInt(), Color.TRANSPARENT) }
    val colors2 by lazy { arrayOf(window.navigationBarColor, 0x88ff0000.toInt(), Color.TRANSPARENT) }

    var nowColorIndex = 0
    var nowColorIndex2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_bars)


        color.setOnClickListener {
            val newColor = colors[++nowColorIndex % 3]
            window.statusBarColor = newColor
            update()
        }
        color2.setOnClickListener {
            val newColor = colors2[++nowColorIndex2 % 3]
            window.navigationBarColor = newColor
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.navigationBarDividerColor = newColor
            }
            update()
        }
        light.setOnClickListener {
            window.isStatusBarLight = !window.isStatusBarLight
            update()
        }
        light2.setOnClickListener {
            window.isNavigationBarLight = !window.isNavigationBarLight
            update()
        }
        contrast.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isStatusBarContrastEnforced = !window.isStatusBarContrastEnforced
            }
            update()
        }
        contrast2.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = !window.isNavigationBarContrastEnforced
            }
            update()
        }
        overlay.setOnClickListener {
            window.isStatusBarOverlay = !window.isStatusBarOverlay
            update()
        }
        overlay2.setOnClickListener {
            window.isNavigationBarOverlay = !window.isNavigationBarOverlay
            update()
        }
        background.setOnClickListener {
            window.isDrawsSystemBarBackgrounds = !window.isDrawsSystemBarBackgrounds
            update()
        }
        immersive_touch.setOnClickListener {
            window.immersive()
            update()
        }
        immersive_swipe.setOnClickListener {
            window.immersive(swipe = true)
            update()
        }
        immersive_transient.setOnClickListener {
            window.immersive(swipe = true, transientBars = true)
            update()
        }
        update()
    }


    @SuppressLint("SetTextI18n")
    private fun update() {
        color.text = "COLOR(${window.statusBarColor.toUInt().toString(16)}) "
        light.text = "LIGHT(${window.isStatusBarLight}) "
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contrast.text = "CONTRAST(${window.isStatusBarContrastEnforced}) "
        }
        overlay.text = "OVERLAY(${window.isStatusBarOverlay})"




        color2.text = "COLOR(${window.navigationBarColor.toUInt().toString(16)}) "
        light2.text = "LIGHT(${window.isNavigationBarLight}) "
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contrast2.text = "CONTRAST(${window.isNavigationBarContrastEnforced}) "
        }
        overlay2.text = "OVERLAY(${window.isNavigationBarOverlay})"



        background.text = "BACKGROUND(${window.isDrawsSystemBarBackgrounds}) "
    }
}
