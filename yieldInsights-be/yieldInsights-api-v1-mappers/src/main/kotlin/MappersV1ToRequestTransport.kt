import models.Deposit
import models.DepositId
import ru.otus.yieldInsights.api.v1.models.DepositCreateObject
import ru.otus.yieldInsights.api.v1.models.DepositReadObject
import ru.otus.yieldInsights.api.v1.models.DepositUpdateObject

fun Deposit.toTransportCreate() = DepositCreateObject(
    depositName = depositName.takeIf { it.isNotBlank() },
    rate = rate.takeIf { it.isNotBlank() },
    openingDate = openingDate.takeIf { it.isNotBlank() },
    depositTerm = depositTerm.takeIf { it.isNotBlank() },
    depositAmount = depositAmount.takeIf { it.isNotBlank() },
    depositOperation = depositOperation.toString(),
    isActive = isActive.toString(),
)

fun Deposit.toTransportRead() = DepositReadObject(
    id = depositId.takeIf { it != DepositId.NONE }?.asString(),
)

fun Deposit.toTransportUpdate() = DepositUpdateObject(
    id = depositId.takeIf { it != DepositId.NONE }?.asString(),
    depositName = depositName.takeIf { it.isNotBlank() },
    rate = rate.takeIf { it.isNotBlank() },
    openingDate = openingDate.takeIf { it.isNotBlank() },
    depositTerm = depositTerm.takeIf { it.isNotBlank() },
    depositAmount = depositAmount.takeIf { it.isNotBlank() },
    depositOperation = depositOperation.toString(),
    isActive = isActive.toString(),
)