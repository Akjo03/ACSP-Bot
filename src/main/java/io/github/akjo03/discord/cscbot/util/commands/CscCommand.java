package io.github.akjo03.discord.cscbot.util.commands;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotSubcommand;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.services.JsonService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.commands.argument.CscCommandArgumentParser;
import io.github.akjo03.discord.cscbot.util.commands.argument.CscCommandArgumentValidator;
import io.github.akjo03.discord.cscbot.util.commands.argument.CscCommandArguments;
import io.github.akjo03.discord.cscbot.util.commands.permission.CscCommandPermissionParser;
import io.github.akjo03.discord.cscbot.util.commands.permission.CscCommandPermissionValidator;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class CscCommand {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommand.class);

	private final String name;
	private CscBotCommand definition;

	protected CscCommand(String name) {
		this.name = name;
	}

	public void initialize(@NotNull BotConfigService botConfigService) {
		CscBotCommand definition = botConfigService.getCommand(name, Optional.empty());
		if (definition == null) {
			LOGGER.error("Command definition for " + name + " not found!");
		}
		this.definition = definition;
	}

	public abstract void execute(MessageReceivedEvent event, CscCommandArguments args);

	public void executeInternal(BotConfigService botConfigService, StringsResourceService stringsResourceService, ErrorMessageService errorMessageService, JsonService jsonService, MessageReceivedEvent event, String argStr) {
		if (!definition.isAvailable()) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + name + "\" but it is not available!");

			event.getChannel().sendMessage(errorMessageService.getErrorMessage(
					"errors.command_unavailable.title",
					"errors.command_unavailable.description",
					"CommandsHandler.onMessageReceived",
					Instant.now(),
					Optional.empty(),
					List.of(),
					List.of(
							name
					)
			).toMessageCreateData()).queue();

			return;
		}

		// Parse and validate command permissions
		CscCommandPermissionParser permissionParser = new CscCommandPermissionParser(name, definition.getPermissions());
		CscCommandPermissionValidator permissionValidator = permissionParser.parse();
		if (!permissionValidator.validate(event.getGuildChannel(), event.getMember())) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + name + "\" but was denied!");

			event.getChannel().sendMessage(errorMessageService.getErrorMessage(
					"errors.command_missing_permissions.title",
					"errors.command_missing_permissions.description",
					"CscCommand.executeInternal",
					Instant.now(),
					Optional.empty(),
					List.of(),
					List.of(
							name
					)
			).toMessageCreateData()).queue();

			return;
		}

		// Parse arguments and subcommand
		CscCommandArgumentParser argumentParser = getArgumentParser(botConfigService, stringsResourceService, errorMessageService, event, argStr);
		if (argumentParser == null) {
			return;
		}
		CscCommandArguments arguments = argumentParser.parse(errorMessageService, jsonService, botConfigService, stringsResourceService, event);
		if (arguments == null) {
			return;
		}

		// Validate arguments
		CscCommandArgumentValidator argumentValidator = new CscCommandArgumentValidator(definition, arguments);
		argumentValidator.validate();

		// Execute command
		execute(event, arguments);
	}

	@SuppressWarnings("DuplicatedCode")
	private @Nullable CscCommandArgumentParser getArgumentParser(BotConfigService botConfigService, StringsResourceService stringsResourceService, ErrorMessageService errorMessageService, MessageReceivedEvent event, String argStr) {
		if (definition.getSubcommands().isAvailable()) {
			boolean hasSubcommand = argStr.trim().split(" ").length > 0;
			if (!hasSubcommand) {
				if (definition.getSubcommands().isRequired()) {
					// If subcommand is required, but not provided, send error message
					LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + name + "\" but no subcommand was provided!");

					event.getChannel().sendMessage(errorMessageService.getErrorMessage(
							"errors.subcommand_required.title",
							"errors.subcommand_required.description",
							"CscCommand.getArgumentParser",
							Instant.now(),
							Optional.empty(),
							List.of(),
							List.of(
									name
							)
					).toMessageCreateData()).queue();

					return null;
				} else {
					// If subcommand is not required and not provided, use no subcommand
					List<String> args = List.of(argStr.split(" "));
					return new CscCommandArgumentParser(definition, args);
				}
			} else {
				// If subcommand is provided, use it
				String subcommandStr = argStr.split(" ")[0];

				if (subcommandStr.isBlank()) {
					if (definition.getSubcommands().isRequired()) {
						// If subcommand is required, but not provided, send error message
						LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + name + "\" but no subcommand was provided!");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"errors.subcommand_required.title",
								"errors.subcommand_required.description",
								"CscCommand.getArgumentParser",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										name
								)
						).toMessageCreateData()).queue();

						return null;
					} else {
						// If subcommand is not required and not provided, use no subcommand
						List<String> args = List.of(argStr.split(" "));
						return new CscCommandArgumentParser(definition, args);
					}
				}

				if (!subcommandStr.trim().equals(";")) {
					// Check if subcommand exists
					CscBotSubcommand subcommand = definition.getSubcommands().getDefinitions().stream()
							.filter(subCommandP -> subCommandP.getName().equals(subcommandStr))
							.findFirst()
							.orElse(null);
					if (subcommand == null) {
						// If subcommand does not exist, send error message
						LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + name + "\" but subcommand \"" + subcommandStr + "\" does not exist!");

						String closestSubcommand = botConfigService.closestSubcommand(name, subcommandStr);

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"errors.subcommand_unknown.title",
								"errors.subcommand_unknown.description",
								"CscCommand.getArgumentParser",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										subcommandStr,
										name,
										closestSubcommand != null ? stringsResourceService.getString("errors.special.similar_command", Optional.of(Languages.ENGLISH), closestSubcommand) : ""
								)
						).toMessageCreateData()).queue();

						return null;
					}

					// Check if subcommand is available
					if (!subcommand.isAvailable()) {
						// If subcommand is not available, send error message
						LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + name + "\" but subcommand \"" + subcommandStr + "\" is not available!");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"errors.subcommand_unavailable.title",
								"errors.subcommand_unavailable.description",
								"CscCommand.getArgumentParser",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										subcommandStr,
										name
								)
						).toMessageCreateData()).queue();

						return null;
					}

					// Check permissions for subcommand -> Return and send error message if not allowed
					CscCommandPermissionParser permissionParser = new CscCommandPermissionParser(name, subcommand.getPermissions());
					CscCommandPermissionValidator permissionValidator = permissionParser.parse();
					if (!permissionValidator.validate(event.getGuildChannel(), event.getMember())) {
						LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute subcommand \"" + subcommandStr + "\" of command \"" + name + "\" but was denied!");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"errors.subcommand_missing_permissions.title",
								"errors.subcommand_missing_permissions.description",
								"CscCommand.getArgumentParser",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										subcommandStr,
										name
								)
						).toMessageCreateData()).queue();

						return null;
					}
				} else {
					// If subcommand is not given but ; is present and subcommands are required, send error message
					if (definition.getSubcommands().isRequired()) {
						LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + name + "\" but no subcommand was provided!");

						event.getChannel().sendMessage(errorMessageService.getErrorMessage(
								"errors.subcommand_required.title",
								"errors.subcommand_required.description",
								"CscCommand.getArgumentParser",
								Instant.now(),
								Optional.empty(),
								List.of(),
								List.of(
										name
								)
						).toMessageCreateData()).queue();

						return null;
					}
				}

				// Subcommand args are before ; and command args are after ; if no ; is present, command args are empty
				List<String> splitArgs = Arrays.asList(argStr.split(";"));
				String subcommandArgsStr = !splitArgs.isEmpty() ? splitArgs.get(0).replaceFirst(subcommandStr, "").trim() : "";
				String commandArgsStr = splitArgs.size() > 1 ? splitArgs.get(1).trim() : "";

				// Parse subcommand arguments
				List<String> subcommandArgs = List.of(subcommandArgsStr.split(" "));
				List<String> commandArgs = List.of(commandArgsStr.split(" "));

				return new CscCommandArgumentParser(definition, subcommandStr, subcommandArgs, commandArgs);
			}
		} else {
			// If subcommands are not available, parse arguments
			List<String> args = List.of(argStr.split(" "));
			return new CscCommandArgumentParser(definition, args);
		}
	}
}