package io.github.akjo03.discord.cscbot.util.commands.argument;

import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotSubcommand;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;

import java.util.List;

public class CscCommandArgumentParser {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommandArgumentParser.class);

	private final CscBotCommand commandDefinition;
	private final String argsString;

	public CscCommandArgumentParser(CscBotCommand commandDefinition, String argsString) {
		this.commandDefinition = commandDefinition;
		this.argsString = argsString;
	}

	public CscCommandArguments parse() {
		LOGGER.info("Parsing arguments for command " + commandDefinition.getCommand() + "...");

		boolean hasSubcommands = commandDefinition.getSubcommands().isAvailable();
		boolean requiresSubcommand = commandDefinition.getSubcommands().isRequired();
		List<CscBotSubcommand> subcommands = commandDefinition.getSubcommands().getDefinitions();

		// TODO: Implement argument parsing

		return new CscCommandArguments(List.of());
	}
}