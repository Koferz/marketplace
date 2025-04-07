package ru.otus.yieldInsights.biz

import Context
import CorSettings
import models.Command
import ru.otus.otuskotlin.marketplace.biz.stubs.stubDeleteSuccess
import ru.otus.otuskotlin.marketplace.biz.stubs.stubValidationBadDepositTerm
import ru.otus.otuskotlin.yieldInsights.biz.general.initStatus
import ru.otus.otuskotlin.yieldInsights.biz.general.operation
import ru.otus.otuskotlin.yieldInsights.biz.general.stubs
import ru.otus.otuskotlin.yieldInsights.biz.stubs.*
import ru.otus.otuskotlin.yieldInsights.cor.rootChain

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
        }
        operation("Получить вклад", Command.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
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
        }
        operation("Удалить вклад", Command.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск вклада", Command.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск подходящих предложений для вклада", Command.CLOSING) {
            stubs("Обработка стабов") {
                stubClosingSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }.build()
}

