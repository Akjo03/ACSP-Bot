package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.util.command.CommandInitializer;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArguments;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@EnableLogger
public class HelpCommand extends CscCommand {
	private Logger logger;

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


	}
}