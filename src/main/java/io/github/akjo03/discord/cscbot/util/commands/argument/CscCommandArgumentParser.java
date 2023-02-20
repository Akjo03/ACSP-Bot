package io.github.akjo03.discord.cscbot.util.commands.argument;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.command.argument.CscBotCommandArgument;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.services.JsonService;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;

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

	public @Nullable CscCommandArguments parse(ErrorMessageService errorMessageService, JsonService jsonService, BotConfigService botConfigService, MessageReceivedEvent event) {
		Map<String, String> suppliedCommandArgs = getSuppliedArguments(args, commandDefinition.getArguments(), errorMessageService, event, false);
		if (suppliedCommandArgs == null) {
			return null;
		}
		if (checkRequiredArguments(suppliedCommandArgs, commandDefinition.getArguments(), errorMessageService, event, false)) {
			return null;
		}
		List<CscCommandArgument<?>> parsedCommandArgs = parseArguments(suppliedCommandArgs, commandDefinition.getArguments(), errorMessageService, jsonService, botConfigService, event, false);
		if (parsedCommandArgs == null) {
			return null;
		}

		Map<String, String> suppliedSubcommandArgs = getSuppliedArguments(subcommandArgs, commandDefinition.getSubcommandArguments(subcommand), errorMessageService, event, true);
		if (suppliedSubcommandArgs == null) {
			return null;
		}
		if (checkRequiredArguments(suppliedSubcommandArgs, commandDefinition.getSubcommandArguments(subcommand), errorMessageService, event, true)) {
			return null;
		}
		List<CscCommandArgument<?>> parsedSubcommandArgs = parseArguments(suppliedSubcommandArgs, commandDefinition.getSubcommandArguments(subcommand), errorMessageService, jsonService, botConfigService, event, true);
		if (parsedSubcommandArgs == null) {
			return null;
		}

		LOGGER.success("=> Command Arguments: " + parsedCommandArgs);
		LOGGER.success("=> Subcommand Arguments: " + parsedSubcommandArgs);

		return subcommand != null
				? new CscCommandArguments(subcommand, parsedSubcommandArgs, parsedCommandArgs)
				: new CscCommandArguments(parsedCommandArgs);
	}

	@SuppressWarnings("DuplicatedCode")
	private @Nullable Map<String, String> getSuppliedArguments(List<String> argsList, List<CscBotCommandArgument> argumentDefinitions, ErrorMessageService errorMessageService, MessageReceivedEvent event, boolean isSubcommand) {
		Map<String, String> suppliedArguments = new HashMap<>();
		// Used to check if key-value pairs are given exclusively or at the end of the argument list
		Map<Integer, Boolean> indexTypeMap = new HashMap<>();

		if (argsList == null) {
			return suppliedArguments;
		}
		for (int i = 0; i < argsList.size(); i++) {
			String arg = argsList.get(i);

			if (indexTypeMap.containsValue(true) && !arg.contains("=")) {
				LOGGER.info("Key-value pairs must either be given exclusively or at the end of the argument list.");

				event.getChannel().sendMessage(errorMessageService.getErrorMessage(
						"ERROR_TITLE_COMMAND_ARGUMENTS_INVALID_ORDER",
						"ERROR_DESCRIPTION_COMMAND_ARGUMENTS_INVALID_ORDER",
						"CscCommandArgumentParser.getSuppliedArguments",
						Instant.now(),
						Optional.empty(),
						List.of(),
						List.of()
				).toMessageCreateData()).queue();

				return null;
			}

			if (argsList.size() > argumentDefinitions.size() && !indexTypeMap.containsValue(true)) {
				if (isSubcommand) {
					LOGGER.info("Too many arguments for subcommand \"" + subcommand + "\" of command \"" + commandDefinition.getCommand() + "\".");

					event.getChannel().sendMessage(errorMessageService.getErrorMessage(
							"ERROR_TITLE_SUBCOMMAND_ARGUMENTS_TOO_MANY",
							"ERROR_DESCRIPTION_SUBCOMMAND_ARGUMENTS_TOO_MANY",
							"CscCommandArgumentParser.getSuppliedArguments",
							Instant.now(),
							Optional.empty(),
							List.of(),
							List.of(
									subcommand,
									commandDefinition.getCommand(),
									String.valueOf(commandDefinition.getSubcommandArguments(subcommand).size())
							)
					).toMessageCreateData()).queue();
				} else {
					LOGGER.info("Too many arguments for command \"" + commandDefinition.getCommand() + "\".");

					event.getChannel().sendMessage(errorMessageService.getErrorMessage(
							"ERROR_TITLE_COMMAND_ARGUMENTS_TOO_MANY",
							"ERROR_DESCRIPTION_COMMAND_ARGUMENTS_TOO_MANY",
							"CscCommandArgumentParser.getSuppliedArguments",
							Instant.now(),
							Optional.empty(),
							List.of(),
							List.of(
									commandDefinition.getCommand(),
									String.valueOf(commandDefinition.getArguments().size())
							)
					).toMessageCreateData()).queue();
				}

				return null;
			}

			if (arg.contains("=")) {
				String[] split = arg.split("=", 2);
				String argName = split[0];
				String argValue = split[1];

				if (argumentDefinitions.stream()
						.noneMatch(argDef -> argDef.getName().equals(argName))) {
					if (isSubcommand) {
						LOGGER.info("Argument \"" + argName + "\" is not defined for subcommand \"" + subcommand + "\" of command \"" + commandDefinition.getCommand() + "\".");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"ERROR_TITLE_SUBCOMMAND_UNKNOWN_ARGUMENT",
								"ERROR_DESCRIPTION_SUBCOMMAND_UNKNOWN_ARGUMENT",
								"CscCommandArgumentParser.getSuppliedArguments",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										argName,
										subcommand,
										commandDefinition.getCommand()
								)
						).toMessageCreateData()).queue();
					} else {
						LOGGER.info("Argument \"" + argName + "\" is not defined for command \"" + commandDefinition.getCommand() + "\".");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"ERROR_TITLE_COMMAND_UNKNOWN_ARGUMENT",
								"ERROR_DESCRIPTION_COMMAND_UNKNOWN_ARGUMENT",
								"CscCommandArgumentParser.getSuppliedArguments",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										argName,
										commandDefinition.getCommand()
								)
						).toMessageCreateData()).queue();
					}

					return null;
				}

				if (suppliedArguments.containsKey(argName)) {
					if (isSubcommand) {
						LOGGER.info("Argument \"" + argName + "\" is defined multiple times for subcommand \"" + subcommand + "\" of command \"" + commandDefinition.getCommand() + "\".");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"ERROR_TITLE_SUBCOMMAND_DUPLICATE_ARGUMENT",
								"ERROR_DESCRIPTION_SUBCOMMAND_DUPLICATE_ARGUMENT",
								"CscCommandArgumentParser.getSuppliedArguments",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										argName,
										subcommand,
										commandDefinition.getCommand()
								)
						).toMessageCreateData()).queue();
					} else {
						LOGGER.info("Argument \"" + argName + "\" is defined multiple times for command \"" + commandDefinition.getCommand() + "\".");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"ERROR_TITLE_COMMAND_DUPLICATE_ARGUMENT",
								"ERROR_DESCRIPTION_COMMAND_DUPLICATE_ARGUMENT",
								"CscCommandArgumentParser.getSuppliedArguments",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										argName,
										commandDefinition.getCommand()
								)
						).toMessageCreateData()).queue();
					}

					return null;
				}

				suppliedArguments.put(argName, argValue);
			} else {
				String argName = argumentDefinitions.get(i).getName();

				if (suppliedArguments.containsKey(argName)) {
					if (isSubcommand) {
						LOGGER.info("Argument \"" + argName + "\" is defined multiple times for subcommand \"" + subcommand + "\" of command \"" + commandDefinition.getCommand() + "\".");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"ERROR_TITLE_SUBCOMMAND_DUPLICATE_ARGUMENT",
								"ERROR_DESCRIPTION_SUBCOMMAND_DUPLICATE_ARGUMENT",
								"CscCommandArgumentParser.getSuppliedArguments",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										argName,
										subcommand,
										commandDefinition.getCommand()
								)
						).toMessageCreateData()).queue();
					} else {
						LOGGER.info("Argument \"" + argName + "\" is defined multiple times for command \"" + commandDefinition.getCommand() + "\".");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"ERROR_TITLE_COMMAND_DUPLICATE_ARGUMENT",
								"ERROR_DESCRIPTION_COMMAND_DUPLICATE_ARGUMENT",
								"CscCommandArgumentParser.getSuppliedArguments",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										argName,
										commandDefinition.getCommand()
								)
						).toMessageCreateData()).queue();
					}

					return null;
				}

				suppliedArguments.put(argName, arg);
			}

			indexTypeMap.put(i, arg.contains("="));
		}

		return suppliedArguments;
	}

	private boolean checkRequiredArguments(Map<String, String> suppliedArguments, List<CscBotCommandArgument> argumentDefinitions, ErrorMessageService errorMessageService, MessageReceivedEvent event, boolean isSubcommand) {
		if (isSubcommand && subcommand == null) {
			return false;
		}

		for (CscBotCommandArgument argumentDefinition : argumentDefinitions) {
			if (argumentDefinition.isRequired() && !suppliedArguments.containsKey(argumentDefinition.getName())) {
				if (isSubcommand) {
					LOGGER.info("Required argument \"" + argumentDefinition.getName() + "\" is missing for subcommand \"" + subcommand + "\" of command \"" + commandDefinition.getCommand() + "\".");

					event.getChannel().sendMessage(errorMessageService.getErrorMessage(
							"ERROR_TITLE_SUBCOMMAND_REQUIRED_ARGUMENT_MISSING",
							"ERROR_DESCRIPTION_SUBCOMMAND_REQUIRED_ARGUMENT_MISSING",
							"CscCommandArgumentParser.checkRequiredArguments",
							Instant.now(),
							Optional.empty(),
							List.of(),
							List.of(
									argumentDefinition.getName(),
									subcommand,
									commandDefinition.getCommand()
							)
					).toMessageCreateData()).queue();
				} else {
					LOGGER.info("Required argument " + argumentDefinition.getName() + " is missing for command " + commandDefinition.getCommand() + ".");

					event.getChannel().sendMessage(errorMessageService.getErrorMessage(
							"ERROR_TITLE_COMMAND_REQUIRED_ARGUMENT_MISSING",
							"ERROR_DESCRIPTION_COMMAND_REQUIRED_ARGUMENT_MISSING",
							"CscCommandArgumentParser.checkRequiredArguments",
							Instant.now(),
							Optional.empty(),
							List.of(),
							List.of(
									argumentDefinition.getName(),
									commandDefinition.getCommand()
							)
					).toMessageCreateData()).queue();
				}

				return true;
			}
		}

		return false;
	}

	private @Nullable List<CscCommandArgument<?>> parseArguments(Map<String, String> suppliedArguments, List<CscBotCommandArgument> argumentDefinitions, ErrorMessageService errorMessageService, JsonService jsonService, BotConfigService botConfigService, MessageReceivedEvent event, boolean isSubcommand) {
		List<CscCommandArgument<?>> parsedArguments = new ArrayList<>();

		if (isSubcommand && subcommand == null) {
			return parsedArguments;
		}

		for (CscBotCommandArgument argumentDefinition : argumentDefinitions) {
			String argValue = suppliedArguments.get(argumentDefinition.getName());

			CscCommandArgumentTypes type = CscCommandArgumentTypes.getTypeByName(argumentDefinition.getType());
			if (type == null) {
				LOGGER.error("Argument type " + argumentDefinition.getType() + " is not defined for command \"" + commandDefinition.getCommand() + "\".");
				continue;
			}

			CscCommandArgument<?> parsedArgument = CscCommandArgument.of(argumentDefinition.getName(), type, argValue);
			if (parsedArgument == null) {
				if (isSubcommand) {
					LOGGER.info("Argument \"" + argumentDefinition.getName() + "\" could not be parsed for subcommand \"" + subcommand + "\" of command \"" + commandDefinition.getCommand() + "\".");

					CscCommandArgumentTypes expectedType = CscCommandArgumentTypes.getTypeByName(argumentDefinition.getType());
					event.getChannel().sendMessage(errorMessageService.getErrorMessage(
							"ERROR_TITLE_SUBCOMMAND_ARGUMENT_INVALID_TYPE",
							"ERROR_DESCRIPTION_SUBCOMMAND_ARGUMENT_INVALID_TYPE",
							"CscCommandArgumentParser.parseArguments",
							Instant.now(),
							Optional.empty(),
							List.of(),
							List.of(
									argumentDefinition.getName(),
									subcommand,
									commandDefinition.getCommand(),
									expectedType != null ? botConfigService.getString(expectedType.getNameLabel(), Languages.ENGLISH).getValue() : "null",
									event.getGuild().getId(),
									event.getChannel().getId(),
									expectedType != null ? botConfigService.getString(expectedType.getTooltipLabel(), Languages.ENGLISH).getValue() : "null"

							)
					).toMessageCreateData()).queue();
				} else {
					LOGGER.info("Argument \"" + argumentDefinition.getName() + "\" could not be parsed for command \"" + commandDefinition.getCommand() + "\".");

					CscCommandArgumentTypes expectedType = CscCommandArgumentTypes.getTypeByName(argumentDefinition.getType());
					event.getChannel().sendMessage(errorMessageService.getErrorMessage(
							"ERROR_TITLE_COMMAND_ARGUMENT_INVALID_TYPE",
							"ERROR_DESCRIPTION_COMMAND_ARGUMENT_INVALID_TYPE",
							"CscCommandArgumentParser.parseArguments",
							Instant.now(),
							Optional.empty(),
							List.of(),
							List.of(
									argumentDefinition.getName(),
									commandDefinition.getCommand(),
									expectedType != null ? botConfigService.getString(expectedType.getNameLabel(), Languages.ENGLISH).getValue() : "null",
									event.getGuild().getId(),
									event.getChannel().getId(),
									expectedType != null ? botConfigService.getString(expectedType.getTooltipLabel(), Languages.ENGLISH).getValue() : "null"
							)
					).toMessageCreateData()).queue();
				}

				return null;
			}

			CscBotCommandArgumentData data = jsonService.objectMapper().convertValue(argumentDefinition.getData(), type.getDataClass());
			if (data == null) {
				LOGGER.error("Argument data for argument \"" + argumentDefinition.getName() + "\" could not be parsed for command \"" + commandDefinition.getCommand() + "\".");
				continue;
			}
			if (parsedArgument.getValue() == null) {
				parsedArgument = CscCommandArgument.of(argumentDefinition.getName(), type, data.getDefaultValue());
			}

			parsedArguments.add(parsedArgument);
		}

		return parsedArguments;
	}
}