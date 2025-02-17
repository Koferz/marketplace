package models

import kotlin.jvm.JvmInline

@JvmInline
value class DepositId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = DepositId("")
    }
}