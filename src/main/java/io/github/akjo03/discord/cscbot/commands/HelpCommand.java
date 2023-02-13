package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.discord.cscbot.util.commands.argument.CscCommandArguments;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends CscCommand {
	private static final Logger LOGGER = LoggerManager.getLogger(HelpCommand.class);

	protected HelpCommand() {
		super("help");
	}

	@Override
	public void execute(MessageReceivedEvent event, CscCommandArguments arguments) {
		LOGGER.info("Executing help command...");

		LOGGER.success("Command help successfully executed!");
	}
}