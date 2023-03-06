package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.constants.CscComponentTypes;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigActionRowComponent;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigInteractionButtonComponent;
import io.github.akjo03.discord.cscbot.services.help.CommandHelpService;
import io.github.akjo03.discord.cscbot.util.command.CommandInitializer;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArguments;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableLogger
public class HelpCommand extends CscCommand {
	private Logger logger;

	private CommandHelpService commandHelpService;

	@Autowired
	protected void setCommandHelpService(CommandHelpService commandHelpService) {
		this.commandHelpService = commandHelpService;
	}

	protected HelpCommand() {
		super("help");
	}

	@Override
	public void initialize(@NotNull CommandInitializer initializer) {

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

		CscBotCommand command = getBotConfigService().getCommand(commandName, Optional.empty());
		if (command == null) {
			logger.error("Command not found: " + commandName);
			return;
		}

		CscBotConfigActionRowComponent commandHelpComponent = getBotConfigService().getComponent("COMMAND_HELP_COMPONENT", CscComponentTypes.ACTION_ROW);
		if (commandHelpComponent == null) {
			logger.error("Action row component not found: COMMAND_HELP_COMPONENT");
			return;
		}

		commandHelpComponent.getComponents().stream()
				.map(CscBotConfigInteractionButtonComponent.class::cast)
				.forEach(button -> {
					if (button.getInteractionId().equals("show_arguments")) {
						button.setDisabled(command.getArguments().isEmpty());
					}
					if (button.getInteractionId().equals("show_subcommands")) {
						button.setDisabled(!command.getSubcommands().isAvailable());
					}
				});


	}
}