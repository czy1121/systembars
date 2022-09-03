package me.reezy.cosmo.systembars

import android.annotation.SuppressLint


internal object RomUtil {

    fun isOppo() = !prop("ro.build.version.opporom").isNullOrBlank()

    fun isMiui6(): Boolean {
        try {
            prop("ro.miui.ui.version.name")?.let {
                return Integer.parseInt(it.substring(1)) >= 6
            }
        }catch (e: Exception) {

        }
        return false
    }


    @SuppressLint("PrivateApi")
    private fun prop(key: String): String? {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val method = clazz.getMethod("get", String::class.java)
            return method.invoke(clazz, key) as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}