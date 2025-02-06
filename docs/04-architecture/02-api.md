# API

## Описание сущности deposit (депозит)

1. BankName
2. DepositName
3. Rate - процентная ставка
4. OpeningDate - дата открытия
5. DepositTerm - срок депозита
6. DepositAmount - сумма вклада
7. DepositOperation: Prolongation/Close
8. IsActive: True/False

## Функции (эндпоинты)

1. CRUDS (create, read, update, delete, search) для депозитов (deposit)
2. deposit.closing (опционально): возвращает список депозитов, с заданным количеством дней до продления/окончания 