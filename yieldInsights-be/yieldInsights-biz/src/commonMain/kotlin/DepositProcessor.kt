import models.Command
import models.DepositId
import models.DepositLock
import ru.otus.otuskotlin.marketplace.biz.stubs.stubDeleteSuccess
import ru.otus.otuskotlin.marketplace.biz.stubs.stubValidationBadDepositTerm
import ru.otus.otuskotlin.marketplace.biz.validation.validateIdNotEmpty
import ru.otus.otuskotlin.yieldInsights.biz.general.initStatus
import ru.otus.otuskotlin.yieldInsights.biz.general.operation
import ru.otus.otuskotlin.yieldInsights.biz.general.stubs
import ru.otus.otuskotlin.yieldInsights.biz.stubs.*
import ru.otus.otuskotlin.yieldInsights.biz.validation.*
import ru.otus.otuskotlin.yieldInsights.cor.rootChain
import ru.otus.otuskotlin.yieldInsights.cor.worker

@Suppress("unused", "RedundantSuspendModifier")
class DepositProcessor(private val corSettings: CorSettings = CorSettings.NONE
) {
    suspend fun exec(ctx: Context) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<Context> {
        initStatus("Инициализация статуса")

        operation("Создание вклада", Command.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadDepositTerm("Имитация ошибки валидации поля срока вклада")
                stubValidationBadOpeningDate("Имитация ошибки валидации поля даты открытия")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в depositValidating") { depositValidating = depositRequest.deepCopy() }
                worker("Очистка id") { depositValidating.depositId = DepositId.NONE }
                worker("Очистка заголовка") { depositValidating.depositName = depositValidating.depositName.trim() }
                worker("Очистка описания") { depositValidating.depositAmount = depositValidating.depositAmount.trim() }
                validateDepositNameNotEmpty("Проверка, что заголовок не пуст")
                validateDepositNameHasContent("Проверка символов")
                validateDepositAmountHasContent("Проверка символов")

                finishValidation("Завершение проверок")
            }
        }
        operation("Получить вклад", Command.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в depositValidating") { depositValidating = depositRequest.deepCopy() }
                worker("Очистка id") { depositValidating.depositId = DepositId(depositValidating.depositId.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Изменить вклад", Command.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadDepositTerm("Имитация ошибки валидации поля срока вклада")
                stubValidationBadOpeningDate("Имитация ошибки валидации поля даты открытия")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в depositValidating") { depositValidating = depositRequest.deepCopy() }
                worker("Очистка id") { depositValidating.depositId = DepositId(depositValidating.depositId.asString().trim()) }
                worker("Очистка lock") { depositValidating.lock = DepositLock(depositValidating.lock.asString().trim()) }
                worker("Очистка названия вклада") { depositValidating.depositName = depositValidating.depositName.trim() }
                worker("Очистка срока вклада") { depositValidating.depositAmount = depositValidating.depositAmount.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateDepositNameNotEmpty("Проверка на непустое название вклада")
                validateDepositNameHasContent("Проверка на наличие содержания в названии вклада")
                validateDepositAmountHasContent("Проверка на наличие содержания в поле срока вклада")

                finishValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Удалить вклад", Command.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в depositValidating") {
                    depositValidating = depositRequest.deepCopy()
                }
                worker("Очистка id") { depositValidating.depositId = DepositId(depositValidating.depositId.asString().trim()) }
                worker("Очистка lock") { depositValidating.lock = DepositLock(depositValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск вклада", Command.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в depositFilterValidating") { depositFilterValidating = depositFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishAdFilterValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск подходящих вкладов", Command.CLOSING) {
            stubs("Обработка стабов") {
                stubClosingSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в depositValidating") { depositValidating = depositRequest.deepCopy() }
                worker("Очистка id") { depositValidating.depositId = DepositId(depositValidating.depositId.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishValidation("Успешное завершение процедуры валидации")
            }
        }
    }.build()
}

