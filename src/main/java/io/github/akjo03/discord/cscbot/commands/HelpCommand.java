package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.data.CscBotHelpMessage;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigActionRowComponent;
import io.github.akjo03.discord.cscbot.handlers.help.CommandArgumentsHelpInteractionHandler;
import io.github.akjo03.discord.cscbot.handlers.help.CommandHelpInteractionHandler;
import io.github.akjo03.discord.cscbot.handlers.help.CommandSubcommandsHelpInteractionHandler;
import io.github.akjo03.discord.cscbot.services.BotDataService;
import io.github.akjo03.discord.cscbot.services.DiscordMessageService;
import io.github.akjo03.discord.cscbot.services.help.HelpCommandService;
import io.github.akjo03.discord.cscbot.util.command.CommandInitializer;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArguments;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EnableLogger
public class HelpCommand extends CscCommand {
	private Logger logger;

	private HelpCommandService helpCommandService;
	private DiscordMessageService discordMessageService;
	private BotDataService botDataService;

	@Autowired
	protected void setCommandHelpService(HelpCommandService helpCommandService) {
		this.helpCommandService = helpCommandService;
	}

	@Autowired
	protected void setDiscordMessageService(DiscordMessageService discordMessageService) {
		this.discordMessageService = discordMessageService;
	}

	@Autowired
	protected void setBotDataService(BotDataService botDataService) {
		this.botDataService = botDataService;
	}

	protected HelpCommand() {
		super("help");
	}

	@Override
	public void initialize(@NotNull CommandInitializer initializer) {
		initializer.registerInteractionHandler(CommandHelpInteractionHandler.class);
		initializer.registerInteractionHandler(CommandArgumentsHelpInteractionHandler.class);
		initializer.registerInteractionHandler(CommandSubcommandsHelpInteractionHandler.class);
	}

	@Override
	public void execute(@NotNull MessageReceivedEvent event, @NotNull CscCommandArguments arguments) {
		logger.info("Executing help command...");

		String subcommand = arguments.getSubcommand();
		if (subcommand == null) {
			logger.error("Subcommand is null!");
			return;
		}

		switch (subcommand) {
			case "command" -> onCommandHelp(event, arguments);
			default -> logger.error("Unknown subcommand: " + subcommand);
		}

		logger.success("Command help successfully executed!");
	}

	public void onCommandHelp(@NotNull MessageReceivedEvent event, @NotNull CscCommandArguments arguments) {
		String commandName = arguments.getSubcommandArgument("command", CscCommandArgumentTypes.STRING);
		if (commandName == null) {
			logger.error("Command name is null!");
			return;
		}

		CscBotCommand command = helpCommandService.getCommandByName(commandName, event.getChannel());
		if (command == null) { return; }

		CscBotConfigActionRowComponent commandHelpComponent = helpCommandService.getCommandHelpComponent(command);

		event.getChannel().sendMessage(
				discordMessageService.addComponentsToMessage(
						helpCommandService.getCommandHelpMessage(command).toMessageCreateData(),
						commandHelpComponent
				)
		).queue(sentMessage -> botDataService.addHelpMessage(CscBotHelpMessage.Builder.create()
				.setMessageId(sentMessage.getId())
				.setPath("/help/command/" + command.getCommand())
				.build()
		));
	}
}