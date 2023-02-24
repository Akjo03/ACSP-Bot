package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.discord.cscbot.util.commands.argument.CscCommandArguments;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@EnableLogger
public class ListCommand extends CscCommand {
	private Logger logger;

	protected ListCommand() {
		super("list");
	}

	@Override
	public void execute(MessageReceivedEvent event) {
		logger.info("Executing list command...");

		logger.success("Command help successfully executed!");
	}
}