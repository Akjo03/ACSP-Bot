package io.github.akjo03.discord.cscbot.util.commands.argument;

import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotSubcommand;
import io.github.akjo03.discord.cscbot.data.config.command.argument.CscBotCommandArgument;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CscCommandArgumentParser {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommandArgumentParser.class);

	private final CscBotCommand commandDefinition;
	private final List<String> args;
	private final String subcommand;
	private final List<String> subcommandArgs;

	public CscCommandArgumentParser(CscBotCommand commandDefinition, @NotNull List<String> args) {
		this.commandDefinition = commandDefinition;
		this.args = args.stream()
				.filter(arg -> !arg.isBlank())
				.toList();
		this.subcommand = null;
		this.subcommandArgs = null;
	}

	public CscCommandArgumentParser(CscBotCommand commandDefinition, String subcommand, @NotNull List<String> subcommandArgs, @NotNull List<String> args) {
		this.commandDefinition = commandDefinition;
		this.args = args.stream()
				.filter(arg -> !arg.isBlank())
				.toList();
		this.subcommand = subcommand;
		this.subcommandArgs = subcommandArgs.stream()
				.filter(arg -> !arg.isBlank())
				.toList();
	}

	public CscCommandArguments parse(ErrorMessageService errorMessageService, MessageReceivedEvent event) {
		LOGGER.info("Parsing arguments for command " + commandDefinition.getCommand() + "...");

		// Command structure: "<command> <?subcommand> <subcommandArgs> ; <args>"
		// Any argument can be given in two ways:
		// 1. As a simple value, e.g. "value"
		// 2. As a key-value pair, e.g. "key=value"
		// Key-Value pairs must either be given exclusively or be only used as the last arguments.
		// If simple values are given, they are assigned to the arguments in the order they are defined in the command config.
		// If key-value pairs are given, the key must match the name of an argument defined in the command config.

		// Every argument has a type defined in the command config. The parser will try to parse the given value to the type defined in the config.
		// If the parser fails to parse the value, the argument will be set to null.
		// If the argument is required, the parser will return an error message.

		// After parsing, the validator will be called to validate the parsed arguments.
		// If the validator encounters a null value, it will return an error message. -> Invalid value for the defined type.
		// Each argument has data validation rules defined in the command config. The validator will check if the parsed value matches the rules.
		// If the validator encounters a value that does not match the rules, it will return an error message. -> Invalid value for the defined rules.

		// If the parser encounters an unknown key, it will return an error message. -> Unknown argument.
		// If the parser encounters a key that is defined multiple times, it will return an error message. -> Argument defined multiple times.
		// If the parser encounters a key-value pair that is not the last argument, it will return an error message. -> Key-value pairs must be the last arguments.

		return subcommand != null ?
				new CscCommandArguments(subcommand, parseSubcommandArguments(errorMessageService, event), parseArguments(errorMessageService, event)) :
				new CscCommandArguments(parseArguments(errorMessageService, event));
	}

	private List<CscCommandArgument<?>> parseArguments(ErrorMessageService errorMessageService, MessageReceivedEvent event) {
		List<CscCommandArgument<?>> parsedArgs = new ArrayList<>();

		List<CscBotCommandArgument> commandArgumentDefinitions = commandDefinition.getArguments();



		return parsedArgs;
	}

	private List<CscCommandArgument<?>> parseSubcommandArguments(ErrorMessageService errorMessageService, MessageReceivedEvent event) {
		List<CscCommandArgument<?>> parsedArgs = new ArrayList<>();

		List<CscBotCommandArgument> subcommandArgumentsDefinitions = commandDefinition.getSubcommands().getDefinitions().stream()
				.filter(subcommandDefinition -> subcommandDefinition.getName().equals(subcommand))
				.map(CscBotSubcommand::getArguments)
				.flatMap(List::stream)
				.toList();

		return parsedArgs;
	}
}