package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@EnableLogger
public class HelpCommand extends CscCommand {
	private Logger logger;

	protected HelpCommand() {
		super("help");
	}

	@Override
	public void execute(MessageReceivedEvent event) {
		logger.info("Executing help command...");

		logger.success("Command help successfully executed!");
	}
}