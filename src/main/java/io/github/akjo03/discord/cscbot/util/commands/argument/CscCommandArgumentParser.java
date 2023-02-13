package io.github.akjo03.discord.cscbot.util.commands.argument;

import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;

import java.util.List;

public class CscCommandArgumentParser {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommandArgumentParser.class);

	private final CscBotCommand commandDefinition;
	private final List<String> args;
	private final String subcommand;
	private final List<String> subcommandArgs;

	public CscCommandArgumentParser(CscBotCommand commandDefinition, List<String> args) {
		this.commandDefinition = commandDefinition;
		this.args = args;
		this.subcommand = null;
		this.subcommandArgs = null;
	}

	public CscCommandArgumentParser(CscBotCommand commandDefinition, String subcommand, List<String> subcommandArgs, List<String> args) {
		this.commandDefinition = commandDefinition;
		this.args = args;
		this.subcommand = subcommand;
		this.subcommandArgs = subcommandArgs;
	}

	public CscCommandArguments parse() {
		LOGGER.info("Parsing arguments for command " + commandDefinition.getCommand() + "...");

		return new CscCommandArguments(List.of());
	}
}