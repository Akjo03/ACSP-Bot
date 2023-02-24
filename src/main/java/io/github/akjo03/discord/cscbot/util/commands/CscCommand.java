package io.github.akjo03.discord.cscbot.util.commands;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;
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

	public abstract void execute(MessageReceivedEvent event);

	public void executeInternal(MessageReceivedEvent event, ErrorMessageService errorMessageService) {
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

		// Execute command
		execute(event);
	}
}