package io.github.sor2171.ffmpegkitkmp

import kotlin.reflect.KClass

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class Logger<T : Any>(clz: KClass<T>) {
    fun info(msg: String)
    fun warn(msg: String)
    fun error(msg: String)
}