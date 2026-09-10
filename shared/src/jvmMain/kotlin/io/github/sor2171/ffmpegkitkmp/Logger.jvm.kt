package io.github.sor2171.ffmpegkitkmp

import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Logger<T : Any> actual constructor(clz: KClass<T>) {
    val logger = LoggerFactory.getLogger(clz.java)!!
    actual fun info(msg: String) = logger.info(msg)
    actual fun warn(msg: String) = logger.warn(msg)
    actual fun error(msg: String) = logger.error(msg)
}