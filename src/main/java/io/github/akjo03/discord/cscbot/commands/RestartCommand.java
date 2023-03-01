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
public class RestartCommand extends CscCommand {
	private Logger logger;

	private BotConfigService botConfigService;

	@Autowired
	private void setBotConfigService(BotConfigService botConfigService) {
		this.botConfigService = botConfigService;
	}

	protected RestartCommand() {
		super("restart");
	}

	@Override
	public void execute(MessageReceivedEvent event, CscCommandArguments arguments) {
		logger.info("Executing restart command... Goodbye!");

		Integer delay = arguments.getCommandArgument(name, "delay", CscCommandArgumentTypes.INTEGER);
		if (delay == null) {
			delay = 0;
		}

		ScheduledThreadPoolExecutor restartExec = new ScheduledThreadPoolExecutor(1);
		restartExec.schedule(() -> {
			event.getChannel().sendMessage(botConfigService.getMessage(
					"RESTART_MESSAGE",
					Optional.empty()
			).toMessageCreateData()).submit().thenRun(CscBot::restart);
		}, delay, TimeUnit.SECONDS);
	}
}