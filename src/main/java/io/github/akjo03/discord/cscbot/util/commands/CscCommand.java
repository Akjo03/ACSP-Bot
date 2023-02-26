package io.github.akjo03.discord.cscbot.util.commands;

import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.services.JsonService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.commands.arguments.CscCommandArgument;
import io.github.akjo03.discord.cscbot.util.commands.arguments.CscCommandArgumentParser;
import io.github.akjo03.discord.cscbot.util.commands.permission.CscCommandPermissionParser;
import io.github.akjo03.discord.cscbot.util.commands.permission.CscCommandPermissionValidator;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Getter
public abstract class CscCommand {
	private static Logger LOGGER;

	private final String name;
	private CscBotCommand definition;

	private BotConfigService botConfigService;
	private StringsResourceService stringsResourceService;
	private ErrorMessageService errorMessageService;
	private JsonService jsonService;

	protected CscCommand(String name) {
		LOGGER = LoggerManager.getLogger(getClass());

		this.name = name;
	}

	public void initialize(@NotNull BotConfigService botConfigService) {
		CscBotCommand definition = botConfigService.getCommand(name, Optional.empty());
		if (definition == null) {
			LOGGER.error("Command definition for " + name + " not found!");
		}
		this.definition = definition;
	}

	public void setupServices(
			BotConfigService botConfigService,
			StringsResourceService stringsResourceService,
			ErrorMessageService errorMessageService,
			JsonService jsonService
	) {
		this.botConfigService = botConfigService;
		this.stringsResourceService = stringsResourceService;
		this.errorMessageService = errorMessageService;
		this.jsonService = jsonService;
	}

	public abstract void execute(MessageReceivedEvent event);

	public void executeInternal(MessageReceivedEvent event, String commandArgStr) {
		if (definition == null) {
			LOGGER.error("Command definition for " + name + " not found!");
			return;
		}

		// Check if command is available, if not, send error message
		if (!definition.isAvailable()) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to use unavailable command \"" + name + "\"!");

			event.getChannel().sendMessage(errorMessageService.getErrorMessage(
					"errors.command_unavailable.title",
					"errors.command_unavailable.description",
					List.of(),
					List.of(
							name
					),
					Optional.empty()
			).toMessageCreateData()).queue();

			return;
		}

		// Parse and validate permissions for command
		CscCommandPermissionParser permissionParser = new CscCommandPermissionParser(name, definition.getPermissions());
		CscCommandPermissionValidator permissionValidator = permissionParser.parse();

		// If permission validation fails, send error message
		if (!permissionValidator.validate(event.getGuildChannel(), event.getMember())) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to use command \"" + name + "\" but was denied!");

			event.getChannel().sendMessage(errorMessageService.getErrorMessage(
					"errors.command_missing_permissions.title",
					"errors.command_missing_permissions.description",
					List.of(),
					List.of(
							name
					),
					Optional.empty()
			).toMessageCreateData()).queue();

			return;
		}


		// Setup argument parser
		CscCommandArgumentParser argumentParser = CscCommandArgumentParser.forCommand(name, definition, commandArgStr);
		argumentParser.setupServices(botConfigService, stringsResourceService, errorMessageService, jsonService);

		// Parse the given arguments
		List<CscCommandArgument<?, ?>> commandArguments = argumentParser.parse();

		// Validate all arguments and send error messages if there are any
		List<CscBotConfigMessage> validationMessages = commandArguments.stream()
				.map(commandArgument -> commandArgument.validate(errorMessageService))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
		if (!validationMessages.isEmpty()) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to use command \"" + name + "\" but failed argument validation!");

			validationMessages.forEach(message -> event.getChannel().sendMessage(
					message.toMessageCreateData()
			).queue());

			return;
		}

		// Execute the command
		execute(event);
	}
}