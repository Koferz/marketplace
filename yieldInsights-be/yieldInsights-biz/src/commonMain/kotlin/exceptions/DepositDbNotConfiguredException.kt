package exceptions

import models.WorkMode

class DepositDbNotConfiguredException(val workMode: WorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
