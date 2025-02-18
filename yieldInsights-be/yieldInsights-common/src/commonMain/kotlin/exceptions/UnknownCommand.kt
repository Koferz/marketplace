package exceptions

import models.Command

class UnknownCommand(command: Command) : Throwable("Wrong command $command at mapping toTransport stage")
