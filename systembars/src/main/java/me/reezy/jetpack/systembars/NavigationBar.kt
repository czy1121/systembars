package me.reezy.jetpack.systembars

import android.os.Build
import android.view.Window

class NavigationBar constructor(private val window: Window): Bar {

    override fun color(color: Int): Bar {
        window.navigationBarColor = color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = color
        }
        return this
    }

    override fun overlay(value: Boolean): Bar {
        window.isNavigationBarOverlay = value
        return this
    }

    override fun light(value: Boolean): Bar {
        window.isNavigationBarLight = value
        return this
    }

    override fun contrast(value: Boolean) : Bar {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = value
        }
        return this
    }
}