package io.github.akjo03.discord.cscbot.handlers.help;

import io.github.akjo03.discord.cscbot.data.CscBotHelpMessage;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigActionRowComponent;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
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
public class CommandHelpInteractionHandler extends CscButtonInteractionHandler {
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

	protected CommandHelpInteractionHandler() {
		super(List.of(
				"show_arguments:command_help",
				"show_subcommands:command_help",
				"close:command_help"
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
		String commandName = helpCommandService.getCommandNameFromPath(helpMessage.getPath());
		CscBotCommand command = helpCommandService.getCommandByName(commandName, event.getChannel());
		if (command == null) { return; }

		switch (event.getComponentId()) {
			case "show_arguments:command_help" -> showArguments(event, command);
			case "show_subcommands:command_help" -> showSubcommands(event, command);
			case "close:command_help" -> helpCommandService.closeHelp(event);
		}
	}

	private void showArguments(ButtonInteractionEvent event, CscBotCommand command) {
		CscBotConfigActionRowComponent commandArgumentsHelpComponent = helpCommandService.getCommandArgumentsHelpComponent();
		if (commandArgumentsHelpComponent == null) { return; }

		CscBotConfigMessage commandArgumentsHelpMessage = helpCommandService.getCommandArgumentsHelpMessage(command, event.getMessage());
		if (commandArgumentsHelpMessage == null) { return; }

		event.editMessage(
				discordMessageService.addComponentsToMessage(
						commandArgumentsHelpMessage.toMessageEditData(),
						commandArgumentsHelpComponent
				)
		).queue(sentMessage -> botDataService.setHelpMessagePath(
				event.getMessageId(),
				"/help/command/" + command.getCommand() + "/arguments"
		));
	}

	private void showSubcommands(ButtonInteractionEvent event, CscBotCommand command) {
		CscBotConfigActionRowComponent commandSubcommandsHelpComponent = helpCommandService.getCommandSubcommandsHelpComponent();
		if (commandSubcommandsHelpComponent == null) { return; }

		CscBotConfigMessage commandSubcommandsHelpMessage = helpCommandService.getCommandSubcommandsHelpMessage(command, event.getMessage());
		if (commandSubcommandsHelpMessage == null) { return; }

		event.editMessage(
				discordMessageService.addComponentsToMessage(
						commandSubcommandsHelpMessage.toMessageEditData(),
						commandSubcommandsHelpComponent
				)
		).queue(sentMessage -> botDataService.setHelpMessagePath(
				event.getMessageId(),
				"/help/command/" + command.getCommand() + "/subcommands"
		));
	}
}