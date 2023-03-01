package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArguments;
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
	public void execute(MessageReceivedEvent event, CscCommandArguments arguments) {
		logger.info("Executing list command...");

		Integer page = arguments.getCommandArgument(name, "page", CscCommandArgumentTypes.INTEGER);
		if (page == null) {
			logger.error("Page argument is null!");
			return;
		}

		logger.success("Command help successfully executed!");
	}
}