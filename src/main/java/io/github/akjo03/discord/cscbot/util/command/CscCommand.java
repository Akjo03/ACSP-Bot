package io.github.akjo03.discord.cscbot.util.command;

import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.services.JsonService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArgumentParser;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArguments;
import io.github.akjo03.discord.cscbot.util.command.permission.CscCommandPermissionParser;
import io.github.akjo03.discord.cscbot.util.command.permission.CscCommandPermissionValidator;
import io.github.akjo03.discord.cscbot.util.exception.CscException;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;
import io.github.akjo03.lib.result.Result;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Getter
public abstract class CscCommand {
	private static Logger LOGGER;

	protected final String name;
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

	public abstract void execute(MessageReceivedEvent event, CscCommandArguments args);

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
		Result<CscCommandArgumentParser> argumentParserResult = CscCommandArgumentParser.forCommand(
				name,
				definition,
				commandArgStr,
				event,

				errorMessageService,
				botConfigService,
				stringsResourceService
		);

		// If argument parser setup fails, send error message, otherwise setup services for parsing later
		CscCommandArgumentParser argumentParser = argumentParserResult
				.ifSuccess(result -> result.setupServices(errorMessageService, botConfigService, stringsResourceService))
				.ifError(error -> ((CscException) error).sendMessage(event.getGuildChannel()))
				.getOrElse((CscCommandArgumentParser) null);
		if (argumentParser == null) {
			LOGGER.warn("User " + event.getAuthor().getAsTag() + " tried to use command \"" + name + "\" but getting argument parser failed!");
			return;
		}

		CscCommandArguments arguments = argumentParser.parse();
		if (arguments == null) {
			LOGGER.warn("User " + event.getAuthor().getAsTag() + " tried to use command \"" + name + "\" but parsing arguments failed!");
			return;
		}

		// Execute the command
		execute(event, arguments);
	}
}