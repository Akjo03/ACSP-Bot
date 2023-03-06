package io.github.akjo03.discord.cscbot.util.command.argument;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class CscCommandArguments {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommandArguments.class);

	private final String commandName;
	private final List<CscCommandArgument<?>> arguments;
	private final String subcommand;
	private final List<CscCommandArgument<?>> subcommandArguments;

	private CscCommandArguments(String commandName, List<CscCommandArgument<?>> arguments) {
		this.commandName = commandName;
		this.arguments = arguments;
		this.subcommand = null;
		this.subcommandArguments = null;
	}

	private CscCommandArguments(String commandName, List<CscCommandArgument<?>> arguments, String subcommand, List<CscCommandArgument<?>> subcommandArguments) {
		this.commandName = commandName;
		this.arguments = arguments;
		this.subcommand = subcommand;
		this.subcommandArguments = subcommandArguments;
	}

	public static CscCommandArguments of(String commandName, List<CscCommandArgument<?>> arguments) {
		return new CscCommandArguments(commandName, arguments);
	}

	public static CscCommandArguments of(String commandName, List<CscCommandArgument<?>> arguments, String subcommand, List<CscCommandArgument<?>> subcommandArguments) {
		return new CscCommandArguments(commandName, arguments, subcommand, subcommandArguments);
	}

	@SuppressWarnings("unchecked")
	public <T> @Nullable T getCommandArgument(String argumentName, CscCommandArgumentTypes type) {
		CscCommandArgument<?> commandArgument = arguments.stream()
				.filter(argument -> argument.getName().equals(argumentName))
				.findFirst()
				.orElse(null);
		if (commandArgument == null) {
			LOGGER.error("Failed to find command argument with name \"" + argumentName + "\" in command \"" + commandName + "\"!");
			return null;
		}
		if (!commandArgument.getType().equals(type)) {
			LOGGER.error("Command argument with name \"" + argumentName + "\" in command \"" + commandName + "\" is not of type " + type + "!");
			return null;
		}
		return (T) commandArgument.getValue();
	}

	@SuppressWarnings("unchecked")
	public <T> @Nullable T getSubcommandArgument(String argumentName, CscCommandArgumentTypes type) {
		if (subcommand == null || subcommandArguments == null) {
			LOGGER.error("No subcommand arguments found in command \"" + commandName + "\"!");
			return null;
		}

		CscCommandArgument<?> commandArgument = subcommandArguments.stream()
				.filter(argument -> argument.getName().equals(argumentName))
				.findFirst()
				.orElse(null);
		if (commandArgument == null) {
			LOGGER.error("Failed to find subcommand argument with name \"" + argumentName + "\" in command \"" + commandName + "\" and subcommand \"" + subcommand + "\"!");
			return null;
		}
		if (!commandArgument.getType().equals(type)) {
			LOGGER.error("Subcommand argument with name \"" + argumentName + "\" in command \"" + commandName + "\" and subcommand \"" + subcommand + "\" is not of type " + type + "!");
			return null;
		}
		return (T) commandArgument.getValue();
	}
}