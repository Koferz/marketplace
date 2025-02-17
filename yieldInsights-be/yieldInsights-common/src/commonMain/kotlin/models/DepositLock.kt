package models

import kotlin.jvm.JvmInline

@JvmInline
value class DepositLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = DepositLock("")
    }
}