package com.demo.app

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.demo.app.databinding.ActivitySystemBarsBinding
import me.reezy.cosmo.systembars.*

class SystemBarsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySystemBarsBinding.bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) }

    val colors by lazy { arrayOf(window.statusBarColor, 0x88ff0000.toInt(), Color.TRANSPARENT) }
    val colors2 by lazy { arrayOf(window.navigationBarColor, 0x88ff0000.toInt(), Color.TRANSPARENT) }

    var nowColorIndex = 0
    var nowColorIndex2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_bars)



        binding.color.setOnClickListener {
            val newColor = colors[++nowColorIndex % 3]
            window.statusBarColor = newColor
            update()
        }
        binding.color2.setOnClickListener {
            val newColor = colors2[++nowColorIndex2 % 3]
            window.navigationBarColor = newColor
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.navigationBarDividerColor = newColor
            }
            update()
        }
        binding.light.setOnClickListener {
            window.isStatusBarLight = !window.isStatusBarLight
            update()
        }
        binding.light2.setOnClickListener {
            window.isNavigationBarLight = !window.isNavigationBarLight
            update()
        }
        binding.contrast.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isStatusBarContrastEnforced = !window.isStatusBarContrastEnforced
            }
            update()
        }
        binding.contrast2.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = !window.isNavigationBarContrastEnforced
            }
            update()
        }
        binding.overlay.setOnClickListener {
            window.isStatusBarOverlay = !window.isStatusBarOverlay
            update()
        }
        binding.overlay2.setOnClickListener {
            window.isNavigationBarOverlay = !window.isNavigationBarOverlay
            update()
        }
        binding.background.setOnClickListener {
            window.isDrawsSystemBarBackgrounds = !window.isDrawsSystemBarBackgrounds
            update()
        }
        binding.immersiveTouch.setOnClickListener {
            window.immersive(WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH)
            update()
        }
        binding.immersiveSwipe.setOnClickListener {
            window.immersive(WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE)
            update()
        }
        binding.immersiveTransient.setOnClickListener {
            window.immersive(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE)
            update()
        }
        binding.immersiveExit.setOnClickListener {
            window.immersiveExit()
            update()
        }
        update()
    }


    @SuppressLint("SetTextI18n")
    private fun update() {
        binding.color.text = "COLOR(${window.statusBarColor.toUInt().toString(16)}) "
        binding.light.text = "LIGHT(${window.isStatusBarLight}) "
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.contrast.text = "CONTRAST(${window.isStatusBarContrastEnforced}) "
        }
        binding.overlay.text = "OVERLAY(${window.isStatusBarOverlay})"




        binding.color2.text = "COLOR(${window.navigationBarColor.toUInt().toString(16)}) "
        binding.light2.text = "LIGHT(${window.isNavigationBarLight}) "
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.contrast2.text = "CONTRAST(${window.isNavigationBarContrastEnforced}) "
        }
        binding.overlay2.text = "OVERLAY(${window.isNavigationBarOverlay})"



        binding.background.text = "BACKGROUND(${window.isDrawsSystemBarBackgrounds}) "
    }
}
