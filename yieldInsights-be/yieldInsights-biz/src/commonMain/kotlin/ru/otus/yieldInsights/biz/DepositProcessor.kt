package ru.otus.yieldInsights.biz

import Context
import CorSettings
import models.Command
import ru.otus.otuskotlin.yieldInsights.biz.general.initStatus
import ru.otus.otuskotlin.yieldInsights.biz.general.operation
import ru.otus.otuskotlin.yieldInsights.biz.general.stubs
import ru.otus.otuskotlin.yieldInsights.biz.stubs.stubCreateSuccess
import ru.otus.otuskotlin.yieldInsights.cor.rootChain

@Suppress("unused", "RedundantSuspendModifier")
class DepositProcessor(private val corSettings: CorSettings = CorSettings.NONE
) {
    suspend fun exec(ctx: Context) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<Context> {
        initStatus("Инициализация статуса")

        operation("Создание объявления", Command.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Получить объявление", Command.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Изменить объявление", Command.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Удалить объявление", Command.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск объявлений", Command.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск подходящих предложений для объявления", Command.CLOSING) {
            stubs("Обработка стабов") {
                stubOffersSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }.build()
}

