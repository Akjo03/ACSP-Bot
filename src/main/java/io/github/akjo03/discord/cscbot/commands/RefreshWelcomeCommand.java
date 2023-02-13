package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.welcome.WelcomeMessageService;
import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.discord.cscbot.util.commands.argument.CscCommandArguments;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshWelcomeCommand extends CscCommand {
	private static final Logger LOGGER = LoggerManager.getLogger(RefreshWelcomeCommand.class);

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
	public void execute(MessageReceivedEvent event, CscCommandArguments arguments) {
		LOGGER.info("Executing refreshWelcome command...");

		botConfigService.loadBotConfig();
		welcomeMessageService.updateWelcomeMessages();

		LOGGER.success("Command refreshWelcome successfully executed!");
	}
}