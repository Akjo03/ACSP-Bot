package io.github.akjo03.discord.cscbot.util.commands;

import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentIntegerData;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.util.commands.arguments.CscCommandArgument;
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
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommand.class);

	private final String name;
	private CscBotCommand definition;

	private ErrorMessageService errorMessageService;

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

	public void setupServices(@NotNull ErrorMessageService errorMessageService) {
		this.errorMessageService = errorMessageService;
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

		// Execute the command
		execute(event);
	}
}