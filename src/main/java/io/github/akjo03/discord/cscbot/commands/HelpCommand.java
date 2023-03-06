package io.github.akjo03.discord.cscbot.commands;

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

		logger.success("Command help successfully executed!");
	}
}