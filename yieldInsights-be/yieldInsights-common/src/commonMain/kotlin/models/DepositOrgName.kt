package models

import kotlin.jvm.JvmInline

@JvmInline
value class DepositOrgName(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = DepositOrgName("")
        val T = DepositOrgName("1")
    }
}