package io.github.akjo03.discord.cscbot.util.commands.argument;

import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;

import java.util.List;
import java.util.Optional;

public class CscCommandArgumentValidator {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommandArgumentValidator.class);

	private final CscBotCommand commandDefinition;
	private final CscCommandArguments arguments;

	public CscCommandArgumentValidator(CscBotCommand commandDefinition, CscCommandArguments arguments) {
		this.commandDefinition = commandDefinition;
		this.arguments = arguments;
	}

	public Optional<List<CscCommandArgumentValidationError>> validate() {
		LOGGER.info("Validating arguments...");

		return Optional.empty();
	}
}