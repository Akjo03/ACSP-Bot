package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.discord.cscbot.util.commands.argument.CscCommandArguments;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class ListCommand extends CscCommand {
	private static final Logger LOGGER = LoggerManager.getLogger(ListCommand.class);

	protected ListCommand() {
		super("list");
	}

	@Override
	public void execute(MessageReceivedEvent event, CscCommandArguments arguments) {
		LOGGER.info("Executing list command...");

		LOGGER.success("Command list successfully executed!");
	}
}