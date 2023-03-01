package com.soywiz.korma.geom

import com.soywiz.korma.geom.bezier.*
import com.soywiz.korma.math.*
import kotlin.math.absoluteValue
import kotlin.test.assertSame
import kotlin.test.assertTrue

fun <T : Any> assertEqualsFloat(
    expected: T?,
    actual: T?,
    absoluteTolerance: Double = 0.001,
    message: String = ""
) {
    if (expected is List<*> && actual is List<*> && expected.size != actual.size) {
        throw AssertionError("${expected.size} != ${actual.size} : ${expected}, ${actual}")
    }
    if (!expected.isAlmostEqualsGeneric(actual, absoluteTolerance)) {
        //org.junit.ComparisonFailure: expected:<[a]> but was:<[b]>

        //throw AssertionError("Actual: $actual\nExpected: $expected\nabsoluteTolerance=$absoluteTolerance\n$message")
        throw AssertionError("expected:<[$expected]> but was:<[$actual]>\nabsoluteTolerance=$absoluteTolerance\n$message")
    }
}

private fun <T : Any> T?.isAlmostEqualsGeneric(
    a: T?,
    absoluteTolerance: Double = 0.00001,
): Boolean {
    val e = this
    if (e == null || a == null) return (e == null) && (a == null)
    return when (e) {
        is Point -> e.isAlmostEquals((a as? Point?) ?: return false, absoluteTolerance.toFloat())
        is IPoint -> e.isAlmostEquals((a as? IPoint?) ?: return false, absoluteTolerance)
        is Float -> {
            if (a !is Float?) return false
            if (e.isNaN() && a.isNaN()) return true
            e.isAlmostEquals(a, absoluteTolerance.toFloat())
        }
        is Double -> {
            if (a !is Double?) return false
            if (e.isNaN() && a.isNaN()) return true
            e.isAlmostEquals(a, absoluteTolerance)
        }
        is Bezier -> e.points.isAlmostEqualsGeneric((a as? Bezier)?.points, absoluteTolerance)
        is IPointArrayList -> e.toList().isAlmostEqualsGeneric((a as? IPointArrayList)?.toList(), absoluteTolerance)
        is List<*> -> {
            if (a !is List<*>?) return false
            if (e.size != a.size) return false
            for (n in 0 until e.size) {
                if (!e[n].isAlmostEqualsGeneric(a[n], absoluteTolerance)) return false
            }
            true
        }
        else -> e == a
    }
}

