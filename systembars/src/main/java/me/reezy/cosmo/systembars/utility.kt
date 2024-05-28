@file:Suppress("DEPRECATION")

package me.reezy.cosmo.systembars

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.view.*
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment


fun Activity.systemBars(): Bar = SystemBars(window)
fun Fragment.systemBars(): Bar = SystemBars(requireActivity().window)
fun Dialog.systemBars(): Bar = SystemBars(window!!)

fun Activity.statusBar(): Bar = StatusBar(window)
fun Fragment.statusBar(): Bar = StatusBar(requireActivity().window)
fun Dialog.statusBar(): Bar = StatusBar(window!!)

fun Activity.navigationBar(): Bar = NavigationBar(window)
fun Fragment.navigationBar(): Bar = NavigationBar(requireActivity().window)
fun Dialog.navigationBar(): Bar = NavigationBar(window!!)


// 进入沉浸式
fun Window.immersive(barsBehavior: Int = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE) {
    val ic = WindowCompat.getInsetsController(this, decorView)
    // 重新显示的方式
    ic.systemBarsBehavior = barsBehavior
    // 隐藏系统栏
    ic.hide(WindowInsetsCompat.Type.systemBars())
}

// 退出沉浸式
fun Window.immersiveExit() {
    WindowCompat.getInsetsController(this, decorView).show(WindowInsetsCompat.Type.systemBars())
}



var Window.isDrawsSystemBarBackgrounds: Boolean
    get() = attributes.flags.hasFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    set(value) = setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, value)

var Window.isStatusBarOverlay: Boolean
    get() = decorView.systemUiVisibility.hasFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    set(value) = setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or if (value) View.SYSTEM_UI_FLAG_LAYOUT_STABLE else 0, value)

var Window.isNavigationBarOverlay: Boolean
    get() = decorView.systemUiVisibility.hasFlags(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    set(value) = setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or if (value) View.SYSTEM_UI_FLAG_LAYOUT_STABLE else 0, value)

var Window.isStatusBarLight: Boolean
    get() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> insetsController!!.systemBarsAppearance.hasFlags(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        else -> decorView.systemUiVisibility.hasFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
    set(value) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val flag = WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                insetsController?.setSystemBarsAppearance(if (value) flag else 0, flag)
            }
            else -> setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, value)
        }
    }

var Window.isNavigationBarLight: Boolean
    get() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> insetsController!!.systemBarsAppearance.hasFlags(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> decorView.systemUiVisibility.hasFlags(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        else -> false
    }
    set(value) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val flag = WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                insetsController?.setSystemBarsAppearance(if (value) flag else 0, flag)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR, value)
            else -> {}
        }
    }

fun Int.hasFlags(flags: Int): Boolean = toUInt() and flags.toUInt() == flags.toUInt()

fun Window.setFlags(flags: Int, enable: Boolean) {
    setFlags(if (enable) flags else 0, flags)
}


fun Window.setSystemUiVisibility(flags: Int, enable: Boolean) {
    decorView.systemUiVisibility = if (enable) decorView.systemUiVisibility or flags else decorView.systemUiVisibility and flags.inv()
}
