package io.github.akjo03.discord.cscbot.util.commands;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
		CscBotCommand definition = botConfigService.getCommand(name);
		if (definition == null) {
			throw new IllegalArgumentException("Command definition not found: " + name);
		}
		this.definition = definition;

		permissionValidator = new CscCommandPermissionParser(name, definition.getPermissions()).parse();

		LOGGER.info("Initialized command \"" + name + "\" with permissions: " + permissionValidator);
	}

	public abstract void execute(MessageReceivedEvent event);

	public void executeInternal(MessageReceivedEvent event, List<String> args) {
		// Parse and validate permissions
		if (!permissionValidator.validate(event.getGuildChannel(), event.getMember())) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command " + name + " but was denied!");
			// TODO: Send error message
			return;
		}

		execute(event);
	}
}