package me.reezy.jetpack.systembars

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.view.*
import androidx.core.view.WindowCompat
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




fun Window.immersive(enable: Boolean = true, swipe: Boolean = false, transientBars: Boolean = false) {
    // 在隐藏/显示系统栏时，不希望布局随之改变
    WindowCompat.setDecorFitsSystemWindows(this, !enable)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (enable) {
            // 重新显示的方式
            insetsController?.systemBarsBehavior = when {
                swipe && transientBars -> WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                swipe -> WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE
                else -> WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
            }
            // 隐藏系统栏
            insetsController?.hide(WindowInsets.Type.systemBars())
        } else {
            // 退出沉浸式
            insetsController?.show(WindowInsets.Type.systemBars())
        }
    } else {
        // 隐藏系统栏
        val flags = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        val immersiveFlags = when {
            // 退出沉浸式
            !enable -> View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_IMMERSIVE
            // 重新显示的方式
            swipe && transientBars -> View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            swipe -> View.SYSTEM_UI_FLAG_IMMERSIVE
            else -> 0
        }
        setSystemUiVisibility(flags or immersiveFlags, enable)
    }
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
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> decorView.systemUiVisibility.hasFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        RomUtil.isOppo() -> decorView.systemUiVisibility.hasFlags(0x00000010)
        else -> false
    }
    set(value) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val flag = WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                insetsController?.setSystemBarsAppearance(if (value) flag else 0, flag)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, value)
            RomUtil.isMiui6() -> setDarkModeForMIUI6("EXTRA_FLAG_STATUS_BAR_DARK_MODE", value)
            RomUtil.isOppo() -> {
                // 设置 Oppo 3.0 (Android 5.1) 状态栏的 darkMode
                // https://open.oppomobile.com/wiki/doc#id=10161
                setSystemUiVisibility(0x00000010, value)
            }
            else -> { }
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
            RomUtil.isMiui6() -> setDarkModeForMIUI6("EXTRA_FLAG_NAVIGATION_BAR_DARK_MODE", value)
            else -> {
            }
        }
    }

@OptIn(ExperimentalUnsignedTypes::class)
fun Int.hasFlags(flags: Int): Boolean = toUInt() and flags.toUInt() == flags.toUInt()

fun Window.setFlags(flags: Int, enable: Boolean) {
    setFlags(if (enable) flags else 0, flags)
}

fun Window.setSystemUiVisibility(flags: Int, enable: Boolean) {
    decorView.systemUiVisibility = if (enable) decorView.systemUiVisibility or flags else decorView.systemUiVisibility and flags.inv()
}

@SuppressLint("PrivateApi")
internal fun Window.setDarkModeForMIUI6(key: String, dark: Boolean) {
    try {
        val classLayoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val fieldDarkMode = classLayoutParams.getField(key)
        val flag = fieldDarkMode.getInt(classLayoutParams)

        val setExtraFlags = javaClass.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
        setExtraFlags(this, if (dark) flag else 0, flag)
    } catch (e: Exception) {
    }

}
