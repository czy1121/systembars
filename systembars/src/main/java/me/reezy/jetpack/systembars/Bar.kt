package me.reezy.jetpack.systembars

import androidx.annotation.ColorInt


interface Bar {
    // 设置系统栏背景色
    fun color(@ColorInt color: Int): Bar

    // 设置覆盖在应用内容上
    fun overlay(value: Boolean = true): Bar
    
    // 设置系统栏浅色模式
    fun light(value: Boolean = true): Bar

    // 设置是否在系统纺栏颜色为0时，显示默认的系统栏半透明背景
    fun contrast(value: Boolean = true): Bar
}