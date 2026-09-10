package io.github.sor2171.ffmpegkitkmp

import android.util.Log
import kotlin.reflect.KClass

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Logger<T : Any> actual constructor(clz: KClass<T>) {
    private val tag = clz.simpleName ?: clz.toString()

    actual fun info(msg: String) {
        Log.i(tag, msg)
    }

    actual fun warn(msg: String) {
        Log.w(tag, msg)
    }

    actual fun error(msg: String) {
        Log.e(tag, msg)
    }
}