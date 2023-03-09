package io.github.akjo03.discord.cscbot.handlers.help;

import io.github.akjo03.discord.cscbot.data.CscBotHelpMessage;
import io.github.akjo03.discord.cscbot.services.BotDataService;
import io.github.akjo03.discord.cscbot.services.DiscordMessageService;
import io.github.akjo03.discord.cscbot.services.help.HelpCommandService;
import io.github.akjo03.discord.cscbot.util.interactions.CscButtonInteractionHandler;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableLogger
public class CommandArgumentsHelpInteractionHandler extends CscButtonInteractionHandler {
	private Logger logger;

	private BotDataService botDataService;
	private DiscordMessageService discordMessageService;
	private HelpCommandService helpCommandService;

	@Autowired
	protected void setBotDataService(BotDataService botDataService) {
		this.botDataService = botDataService;
	}

	@Autowired
	protected void setDiscordMessageService(DiscordMessageService discordMessageService) {
		this.discordMessageService = discordMessageService;
	}

	@Autowired
	protected void setCommandHelpService(HelpCommandService helpCommandService) {
		this.helpCommandService = helpCommandService;
	}

	protected CommandArgumentsHelpInteractionHandler() {
		super(List.of(
				"back:command_arguments_help",
				"close:command_arguments_help"
		));
	}

	@Override
	@SuppressWarnings("DuplicatedCode")
	public void onExecute(@NotNull ButtonInteractionEvent event) {
		CscBotHelpMessage helpMessage = botDataService.getHelpMessage(event.getMessageId());
		if (helpMessage == null) {
			logger.warn("Help message not found for message id: " + event.getMessageId());
			return;
		}

		switch (event.getComponentId()) {
			case "back:command_arguments_help" -> helpCommandService.backToCommand(event, helpMessage);
			case "close:command_arguments_help" -> helpCommandService.closeHelp(event);
		}
	}
}