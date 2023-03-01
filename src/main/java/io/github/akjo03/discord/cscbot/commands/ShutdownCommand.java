package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.CscBot;
import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArguments;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@EnableLogger
public class ShutdownCommand extends CscCommand {
	private Logger logger;

	private BotConfigService botConfigService;

	@Autowired
	private void setBotConfigService(BotConfigService botConfigService) {
		this.botConfigService = botConfigService;
	}

	protected ShutdownCommand() {
		super("shutdown");
	}

	@Override
	public void execute(MessageReceivedEvent event, CscCommandArguments arguments) {
		logger.info("Executing shutdown command... Goodbye!");

		Integer delay = arguments.getCommandArgument(name, "delay", CscCommandArgumentTypes.INTEGER);
		if (delay == null) {
			delay = 0;
		}

		ScheduledThreadPoolExecutor shutdownExec = new ScheduledThreadPoolExecutor(1);
		shutdownExec.schedule(() -> {
			event.getChannel().sendMessage(botConfigService.getMessage(
					"SHUTDOWN_MESSAGE",
					Optional.empty()
			).toMessageCreateData()).submit().thenRun(CscBot::shutdown);
		}, delay, TimeUnit.SECONDS);
	}
}