package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.welcome.WelcomeMessageService;
import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EnableLogger
public class RefreshWelcomeCommand extends CscCommand {
	private Logger logger;

	private BotConfigService botConfigService;
	private WelcomeMessageService welcomeMessageService;

	@Autowired
	public void setBotConfigService(BotConfigService botConfigService) {
		this.botConfigService = botConfigService;
	}

	@Autowired
	public void setWelcomeMessageService(WelcomeMessageService welcomeMessageService) {
		this.welcomeMessageService = welcomeMessageService;
	}

	protected RefreshWelcomeCommand() {
		super("refreshWelcome");
	}

	@Override
	public void execute(MessageReceivedEvent event) {
		logger.info("Executing refreshWelcome command...");

		botConfigService.loadBotConfig();
		welcomeMessageService.updateWelcomeMessages();

		logger.success("Command refreshWelcome successfully executed!");
	}
}