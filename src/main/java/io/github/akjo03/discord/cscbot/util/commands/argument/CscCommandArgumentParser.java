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