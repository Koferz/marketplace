package models

data class DepositFilter(
    var searchString: String = "",
    val depositRate: String = ""
)  {
    fun deepCopy(): DepositFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = DepositFilter()
    }
}
