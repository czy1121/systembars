package me.reezy.cosmo.systembars

import android.os.Build
import android.view.Window


class StatusBar(private val window: Window): Bar {

    override fun color(color: Int) : Bar {
        window.statusBarColor = color
        return this
    }

    override fun overlay(value: Boolean) : Bar {
        window.isStatusBarOverlay = value
        return this
    }

    override fun light(value: Boolean) : Bar {
        window.isStatusBarLight = value
        return this
    }
    override fun contrast(value: Boolean) : Bar {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = value
        }
        return this
    }
}