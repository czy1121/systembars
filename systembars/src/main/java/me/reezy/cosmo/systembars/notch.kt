package me.reezy.cosmo.systembars

import android.app.Activity
import android.os.Build
import java.lang.reflect.Method

/**
 * 是否有刘海屏
 *
 */
val Activity.hasNotch: Boolean
    get() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> window.decorView.rootWindowInsets?.displayCutout != null
        Build.MANUFACTURER.equals("huawei", ignoreCase = true) -> hasNotchHw(this)
        Build.MANUFACTURER.equals("xiaomi", ignoreCase = true) -> hasNotchXiaoMi(this)
        Build.MANUFACTURER.equals("oppo", ignoreCase = true) -> hasNotchOPPO(this)
        Build.MANUFACTURER.equals("vivo", ignoreCase = true) -> hasNotchVIVO(this)
        else -> false
    }

/**
 * 判断vivo是否有刘海屏
 * https://swsdl.vivo.com.cn/appstore/developer/uploadfile/20180328/20180328152252602.pdf
 *
 * @param activity
 * @return
 */
private fun hasNotchVIVO(activity: Activity): Boolean {
    return try {
        val clazz = Class.forName("android.util.FtFeature")
        val method: Method = clazz.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
        method.invoke(clazz, 0x20) as Boolean
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * 判断oppo是否有刘海屏
 * https://open.oppomobile.com/wiki/doc#id=10159
 *
 * @param activity
 * @return
 */
private fun hasNotchOPPO(activity: Activity): Boolean {
    return activity.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
}

/**
 * 判断xiaomi是否有刘海屏
 * https://dev.mi.com/console/doc/detail?pId=1293
 *
 * @param activity
 * @return
 */
private fun hasNotchXiaoMi(activity: Activity): Boolean {
    return try {
        val c = Class.forName("android.os.SystemProperties")
        val get: Method = c.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
        get.invoke(c, "ro.miui.notch", 0) as Int == 1
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * 判断华为是否有刘海屏
 * https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114
 *
 * @param activity
 * @return
 */
private fun hasNotchHw(activity: Activity): Boolean {
    return try {
        val cl = activity.classLoader
        val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
        val get: Method = HwNotchSizeUtil.getMethod("hasNotchInScreen")
        get.invoke(HwNotchSizeUtil) as Boolean
    } catch (e: Exception) {
        false
    }
}