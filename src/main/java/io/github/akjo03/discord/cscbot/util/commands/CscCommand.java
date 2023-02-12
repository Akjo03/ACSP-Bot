package io.github.akjo03.discord.cscbot.util.commands;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class CscCommand {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommand.class);

	private final String name;
	private CscBotCommand definition;

	private CscCommandPermissionValidator permissionValidator;

	protected CscCommand(String name) {
		this.name = name;
	}

	public void initialize(@NotNull BotConfigService botConfigService) {
		CscBotCommand definition = botConfigService.getCommand(name, Optional.empty());
		if (definition == null) {
			throw new IllegalArgumentException("Command definition not found: " + name);
		}
		this.definition = definition;

		permissionValidator = new CscCommandPermissionParser(name, definition.getPermissions()).parse();

		LOGGER.info("Initialized command \"" + name + "\" with permissions: " + permissionValidator);
	}

	public abstract void execute(MessageReceivedEvent event);

	public void executeInternal(BotConfigService botConfigService, ErrorMessageService errorMessageService, MessageReceivedEvent event, List<String> args) {
		// Parse and validate permissions
		if (!permissionValidator.validate(event.getGuildChannel(), event.getMember())) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command " + name + " but was denied!");

			// TODO: Language support
			event.getChannel().sendMessage(errorMessageService.getErrorMessage(
					"ERROR_TITLE_COMMAND_MISSING_PERMISSIONS",
					"ERROR_DESCRIPTION_COMMAND_MISSING_PERMISSIONS",
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

		execute(event);
	}
}