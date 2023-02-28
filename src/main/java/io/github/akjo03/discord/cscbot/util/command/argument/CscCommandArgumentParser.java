package io.github.akjo03.discord.cscbot.util.command.argument;

import com.google.common.collect.Lists;
import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotSubcommand;
import io.github.akjo03.discord.cscbot.data.config.command.argument.CscBotCommandArgument;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentIntegerData;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentStringData;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.choice.CscBotCommandArgumentChoiceData;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.command.permission.CscCommandPermissionParser;
import io.github.akjo03.discord.cscbot.util.command.permission.CscCommandPermissionValidator;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentParseException;
import io.github.akjo03.discord.cscbot.util.exception.CscException;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;
import io.github.akjo03.lib.result.Result;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CscCommandArgumentParser {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommandArgumentParser.class);

	private final String commandName;
	private final CscBotCommand commandDefinition;
	private final List<String> args;
	private final String subcommand;
	private final List<String> subcommandArgs;

	private final MessageReceivedEvent event;

	private BotConfigService botConfigService;
	private ErrorMessageService errorMessageService;
	private StringsResourceService stringsResourceService;

	private CscCommandArgumentParser(String commandName, CscBotCommand commandDefinition, List<String> args, MessageReceivedEvent event) {
		this.commandName = commandName;
		this.commandDefinition = commandDefinition;
		this.args = args;
		this.subcommand = null;
		this.subcommandArgs = null;
		this.event = event;
	}

	private CscCommandArgumentParser(String commandName, CscBotCommand commandDefinition, List<String> args, String subcommand, List<String> subcommandArgs, MessageReceivedEvent event) {
		this.commandName = commandName;
		this.commandDefinition = commandDefinition;
		this.args = args;
		this.subcommand = subcommand;
		this.subcommandArgs = subcommandArgs;
		this.event = event;
	}

	public static Result<CscCommandArgumentParser> forCommand(
			String commandName,
			CscBotCommand commandDefinition,
			String argStr,
			MessageReceivedEvent event,

			ErrorMessageService errorMessageService,
			BotConfigService botConfigService,
			StringsResourceService stringsResourceService
	) {
		// If we don't have subcommands, just parse the arguments
		if (!commandDefinition.getSubcommands().isAvailable()) {
			List<String> args = List.of(argStr.split(" "));
			return Result.success(new CscCommandArgumentParser(commandName, commandDefinition, args, event));
		}

		boolean hasSubcommand = argStr.trim().split(" ").length > 0;

		// If we have subcommands, but the user didn't provide one, check if it's required
		if (!hasSubcommand) {
			return checkIfSubcommandsRequired(commandName, commandDefinition, argStr, event, errorMessageService);
		}

		// Get the subcommand name
		String subcommandName = argStr.trim().split(" ")[0];

		// If the subcommand name is empty or just the argument separator, check if it's required
		if (subcommandName.isEmpty()) {
			return checkIfSubcommandsRequired(commandName, commandDefinition, argStr, event, errorMessageService);
		}
		if (subcommandName.trim().equals(";")) {
			return checkIfSubcommandsRequired(commandName, commandDefinition, argStr, event, errorMessageService);
		}

		// Get the subcommand definition
		CscBotSubcommand subcommandDefinition = commandDefinition.getSubcommands().getDefinitions().stream()
				.filter(subcommand -> subcommand.getName().equals(subcommandName))
				.findFirst()
				.orElse(null);

		// If the subcommand doesn't exist, send an error message
		if (subcommandDefinition == null) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + commandName + "\" but subcommand \"" + subcommandName + "\" does not exist!");

			String closestSubcommand = botConfigService.closestSubcommand(commandName, subcommandName);

			return Result.fail(new CscException(
					"errors.subcommand_unknown.title",
					"errors.subcommand_unknown.description",
					List.of(),
					List.of(
							subcommandName,
							commandName,
							closestSubcommand != null ? stringsResourceService.getString("errors.special.similar_command", Optional.of(Languages.ENGLISH), closestSubcommand) : ""
					),
					null,
					errorMessageService
			));
		}

		// If the subcommand exists, but is currently not available, send an error message
		if (!subcommandDefinition.isAvailable()) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + commandName + "\" but subcommand \"" + subcommandName + "\" is not available!");

			return Result.fail(new CscException(
					"errors.subcommand_unavailable.title",
					"errors.subcommand_unavailable.description",
					List.of(),
					List.of(subcommandName, commandName),
					null,
					errorMessageService
			));
		}

		// Check permissions for subcommand and send error message if user doesn't have permission
		CscCommandPermissionParser permissionParser = new CscCommandPermissionParser(subcommandName, subcommandDefinition.getPermissions());
		CscCommandPermissionValidator permissionValidator = permissionParser.parse();
		if (!permissionValidator.validate(event.getGuildChannel(), event.getMember())) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + commandName + "\" but subcommand \"" + subcommandName + "\" is not available!");

			return Result.fail(new CscException(
					"errors.subcommand_missing_permissions.title",
					"errors.subcommand_missing_permissions.description",
					List.of(),
					List.of(subcommandName, commandName),
					null,
					errorMessageService
			));
		}

		// Subcommand args are before ; and command args are after ; if no ; is present, command args are empty
		List<String> splitArgs = Arrays.asList(argStr.split(";"));
		String subcommandArgsStr = !splitArgs.isEmpty() ? splitArgs.get(0).replaceFirst(subcommandName, "").trim() : "";
		String commandArgsStr = splitArgs.size() > 1 ? splitArgs.get(1).trim() : "";

		// Split the subcommand args and command args
		List<String> subcommandArgs = Stream.of(subcommandArgsStr.split(" "))
				.filter(arg -> !arg.isEmpty())
				.collect(Collectors.toList());
		List<String> commandArgs = Stream.of(commandArgsStr.split(" "))
				.filter(arg -> !arg.isEmpty())
				.collect(Collectors.toList());

		return Result.success(new CscCommandArgumentParser(commandName, commandDefinition, commandArgs, subcommandName, subcommandArgs, event));
	}

	@NotNull
	private static Result<CscCommandArgumentParser> checkIfSubcommandsRequired(String commandName, CscBotCommand commandDefinition, String argStr, MessageReceivedEvent event, ErrorMessageService errorMessageService) {
		if (commandDefinition.getSubcommands().isRequired()) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to use command " + commandName + " without a subcommand!");

			return Result.fail(new CscException(
					"errors.subcommand_required.title",
					"errors.subcommand_required.description",
					List.of(),
					List.of(commandName),
					null,
					errorMessageService
			));
		}

		List<String> args = List.of(argStr.split(" "));
		return Result.success(new CscCommandArgumentParser(commandName, commandDefinition, args, event));
	}

	public void setupServices(ErrorMessageService errorMessageService, BotConfigService botConfigService, StringsResourceService stringsResourceService) {
		this.errorMessageService = errorMessageService;
		this.botConfigService = botConfigService;
		this.stringsResourceService = stringsResourceService;
	}

	public CscCommandArguments parse() {
		Map<String, String> suppliedArgs = getSuppliedArguments(args, commandDefinition.getArguments(), false);
		if (suppliedArgs == null) {
			return null;
		}
		if (checkRequiredArguments(suppliedArgs, commandDefinition.getArguments(), false)) {
			return null;
		}
		List<CscCommandArgument<?>> commandArgs = parseArguments(suppliedArgs, commandDefinition.getArguments(), false);
		if (commandArgs == null) {
			return null;
		}

		Map<String, String> suppliedSubcommandArgs = getSuppliedArguments(subcommandArgs, commandDefinition.getSubcommandArguments(subcommand), true);
		if (suppliedSubcommandArgs == null) {
			return null;
		}
		if (checkRequiredArguments(suppliedSubcommandArgs, commandDefinition.getSubcommandArguments(subcommand), true)) {
			return null;
		}
		List<CscCommandArgument<?>> subcommandArgs = parseArguments(suppliedSubcommandArgs, commandDefinition.getSubcommandArguments(subcommand), true);
		if (subcommandArgs == null) {
			return null;
		}

		return subcommand != null
				? CscCommandArguments.of(commandArgs, subcommand, subcommandArgs)
				: CscCommandArguments.of(commandArgs);
	}

	private @Nullable Map<String, String> getSuppliedArguments(List<String> argsList, List<CscBotCommandArgument<?>> argumentDefinitions, boolean isSubcommand) {
		Map<String, String> suppliedArguments = new HashMap<>();
		// Used to check if key-value pairs are given exclusively or at the end of the argument list
		Map<Integer, Boolean> indexTypeMap = new HashMap<>();

		if (argsList == null || argsList.isEmpty()) {
			return suppliedArguments;
		}

		for (int i = 0; i < argsList.size(); i++) {
			String arg = argsList.get(i);

			if (indexTypeMap.containsValue(true) && !arg.contains("=")) {
				event.getChannel().sendMessage(errorMessageService.getErrorMessage(
						"errors.command_arguments_invalid_order.title",
						"errors.command_arguments_invalid_order.description",
						List.of(),
						List.of(),
						Optional.empty()
				).toMessageCreateData()).queue();
				return null;
			}

			if (argsList.size() > argumentDefinitions.size() && !indexTypeMap.containsValue(true)) {
				event.getChannel().sendMessage(getErrorMessage(
						"errors.command_arguments_too_many",
						"errors.subcommand_arguments_too_many",
						Lists.newArrayList(),
						Lists.newArrayList(
								commandName,
								String.valueOf(argumentDefinitions.size())
						),
						Lists.newArrayList(),
						Lists.newArrayList(
								subcommand,
								commandName,
								String.valueOf(argumentDefinitions.size())
						),
						isSubcommand,
						null
				).toMessageCreateData()).queue();
				return null;
			}

			if (arg.contains("=")) {
				Map.Entry<String, String> namedArg = getNamedArgument(arg, suppliedArguments, argumentDefinitions, isSubcommand);
				if (namedArg == null) {
					return null;
				}
				suppliedArguments.put(namedArg.getKey(), namedArg.getValue());
				indexTypeMap.put(i, false);
			} else {
				Map.Entry<String, String> indexedArg = getIndexedArgument(arg, i, suppliedArguments, argumentDefinitions, isSubcommand);
				if (indexedArg == null) {
					return null;
				}
				suppliedArguments.put(indexedArg.getKey(), indexedArg.getValue());
				indexTypeMap.put(i, true);
			}
		}

		return suppliedArguments;
	}

	private boolean checkRequiredArguments(Map<String, String> suppliedArguments, List<CscBotCommandArgument<?>> argumentDefinitions, boolean isSubcommand) {
		if (isSubcommand && subcommand == null) {
			return false;
		}

		for (CscBotCommandArgument<?> argumentDefinition : argumentDefinitions) {
			if (argumentDefinition.isRequired() && !suppliedArguments.containsKey(argumentDefinition.getName())) {
				event.getChannel().sendMessage(getErrorMessage(
						"errors.command_required_argument_missing",
						"errors.subcommand_required_argument_missing",
						Lists.newArrayList(),
						Lists.newArrayList(
								commandName,
								argumentDefinition.getName()
						),
						Lists.newArrayList(),
						Lists.newArrayList(
								subcommand,
								commandName,
								argumentDefinition.getName()
						),
						isSubcommand,
						null
				).toMessageCreateData()).queue();
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("DuplicatedCode")
	private @Nullable List<CscCommandArgument<?>> parseArguments(Map<String, String> suppliedArguments, List<CscBotCommandArgument<?>> argumentDefinitions, boolean isSubcommand) {
		if (isSubcommand && subcommand == null) {
			return List.of();
		}

		List<CscCommandArgument<?>> parsedArguments = new ArrayList<>();
		List<CscCommandArgumentParseException> parseExceptions = new ArrayList<>();

		for (CscBotCommandArgument<?> argumentDefinition : argumentDefinitions) {
			String argTypeStr = argumentDefinition.getType();
			CscCommandArgumentTypes argType = CscCommandArgumentTypes.fromString(argTypeStr);
			if (argType == null) {
				LOGGER.error("Failed to find argument type for argument " + argumentDefinition.getName() + " in command " + commandName + ": " + argTypeStr);
				continue;
			}

			switch (argType) {
				case INTEGER -> {
					CscBotCommandArgumentIntegerData argData = (CscBotCommandArgumentIntegerData) argumentDefinition.getData();
					Result<Integer> intResult = argData.parse(
							commandName, argumentDefinition.getName(),
							suppliedArguments.get(argumentDefinition.getName()),
							event, botConfigService, stringsResourceService
					);
					if (intResult.isError()) {
						if (intResult.getError() instanceof CscCommandArgumentParseException) {
							parseExceptions.add((CscCommandArgumentParseException) intResult.getError());
						} else {
							LOGGER.error("Unknown error while parsing choice argument " + argumentDefinition.getName() + " in command " + commandName, intResult.getError());
							return null;
						}
						break;
					}
					CscCommandArgument<Integer> integerArgument = CscCommandArgument.of(argumentDefinition.getName(), argumentDefinition.getDescription(), intResult.get(), argData, argType);
					parsedArguments.add(integerArgument);
				}
				case STRING -> {
					CscBotCommandArgumentStringData argData = (CscBotCommandArgumentStringData) argumentDefinition.getData();
					Result<String> stringResult = argData.parse(
							commandName, argumentDefinition.getName(),
							suppliedArguments.get(argumentDefinition.getName()),
							event, botConfigService, stringsResourceService
					);
					if (stringResult.isError()) {
						if (stringResult.getError() instanceof CscCommandArgumentParseException) {
							parseExceptions.add((CscCommandArgumentParseException) stringResult.getError());
						} else {
							LOGGER.error("Unknown error while parsing choice argument " + argumentDefinition.getName() + " in command " + commandName, stringResult.getError());
							return null;
						}
						break;
					}
					CscCommandArgument<String> stringArgument = CscCommandArgument.of(argumentDefinition.getName(), argumentDefinition.getDescription(), stringResult.get(), argData, argType);
					parsedArguments.add(stringArgument);
				}
				case CHOICE -> {
					CscBotCommandArgumentChoiceData argData = (CscBotCommandArgumentChoiceData) argumentDefinition.getData();
					Result<String> choiceResult = argData.parse(
							commandName, argumentDefinition.getName(),
							suppliedArguments.get(argumentDefinition.getName()),
							event, botConfigService, stringsResourceService
					);
					if (choiceResult.isError()) {
						if (choiceResult.getError() instanceof CscCommandArgumentParseException) {
							parseExceptions.add((CscCommandArgumentParseException) choiceResult.getError());
						} else {
							LOGGER.error("Unknown error while parsing choice argument " + argumentDefinition.getName() + " in command " + commandName, choiceResult.getError());
							return null;
						}
						break;
					}
					CscCommandArgument<String> choiceArgument = CscCommandArgument.of(argumentDefinition.getName(), argumentDefinition.getDescription(), choiceResult.get(), argData, argType);
					parsedArguments.add(choiceArgument);
				}
				default -> LOGGER.error("Unknown argument type " + argTypeStr + " for argument " + argumentDefinition.getName() + " in command " + commandName);
			}
		}

		if (!parseExceptions.isEmpty()) {
			event.getChannel().sendMessage(errorMessageService.getCommandArgumentParseErrorMessage(parseExceptions,
					Optional.empty()
			).toMessageCreateData()).queue();
			return null;
		}

		return parsedArguments;
	}

	private @Nullable Map.Entry<String, String> getNamedArgument(String arg, Map<String, String> existingArguments, List<CscBotCommandArgument<?>> argumentDefinitions, boolean isSubcommand) {
		String[] split = arg.split("=", 2);
		String argName = split[0];
		String argValue = split[1];

		if (argumentDefinitions.stream().noneMatch(argDef -> argDef.getName().equals(argName))) {
			event.getChannel().sendMessage(getErrorMessage(
					"errors.command_unknown_argument",
					"errors.subcommand_unknown_argument",
					Lists.newArrayList(),
					Lists.newArrayList(
							argName,
							commandName
					),
					Lists.newArrayList(),
					Lists.newArrayList(
							argName,
							subcommand,
							commandName
					),
					isSubcommand,
					null
			).toMessageCreateData()).queue();
			return null;
		}

		if (checkDuplicateArgument(argName, existingArguments, isSubcommand)) {
			return null;
		}

		return Map.entry(argName, argValue);
	}

	private @Nullable Map.Entry<String, String> getIndexedArgument(String arg, int index, Map<String, String> existingArguments, List<CscBotCommandArgument<?>> argumentDefinitions, boolean isSubcommand) {
		String argName = argumentDefinitions.get(index).getName();

		if (checkDuplicateArgument(argName, existingArguments, isSubcommand)) {
			return null;
		}

		return Map.entry(argName, arg);
	}

	private boolean checkDuplicateArgument(String argName, Map<String, String> existingArguments, boolean isSubcommand) {
		if (existingArguments.containsKey(argName)) {
			event.getChannel().sendMessage(getErrorMessage(
					"errors.command_duplicate_argument",
					"errors.subcommand_duplicate_argument",
					Lists.newArrayList(),
					Lists.newArrayList(
							argName,
							commandName
					),
					Lists.newArrayList(),
					Lists.newArrayList(
							argName,
							subcommand,
							commandName
					),
					isSubcommand,
					null
			).toMessageCreateData()).queue();
			return true;
		}

		return false;
	}

	private CscBotConfigMessage getErrorMessage(
			String commandKey,
			String subcommandKey,
			List<String> commandErrorTitlePlaceholders,
			List<String> commandErrorDescriptionPlaceholders,
			List<String> subcommandErrorTitlePlaceholders,
			List<String> subcommandErrorDescriptionPlaceholders,
			boolean isSubcommand,
			Languages language
	) {
		if (isSubcommand) {
			String titleKey = subcommandKey + ".title";
			String descriptionKey = subcommandKey + ".description";
			return errorMessageService.getErrorMessage(
					titleKey,
					descriptionKey,
					subcommandErrorTitlePlaceholders,
					subcommandErrorDescriptionPlaceholders,
					Optional.ofNullable(language)
			);
		} else {
			String titleKey = commandKey + ".title";
			String descriptionKey = commandKey + ".description";
			return errorMessageService.getErrorMessage(
					titleKey,
					descriptionKey,
					commandErrorTitlePlaceholders,
					commandErrorDescriptionPlaceholders,
					Optional.ofNullable(language)
			);
		}
	}
}