package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.rules.RulesMessageService;
import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.discord.cscbot.util.commands.argument.CscCommandArguments;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshRulesCommand extends CscCommand {
	private static final Logger LOGGER = LoggerManager.getLogger(RefreshRulesCommand.class);

	private BotConfigService botConfigService;
	private RulesMessageService rulesMessageService;

	@Autowired
	public void setBotConfigService(BotConfigService botConfigService) {
		this.botConfigService = botConfigService;
	}

	@Autowired
	public void setRulesMessageService(RulesMessageService rulesMessageService) {
		this.rulesMessageService = rulesMessageService;
	}

	protected RefreshRulesCommand() {
		super("refreshRules");
	}

	@Override
	public void execute(MessageReceivedEvent event, CscCommandArguments arguments) {
		LOGGER.info("Executing refreshRules command...");

		botConfigService.loadBotConfig();
		rulesMessageService.updateRulesMessages();

		LOGGER.success("Command refreshRules successfully executed!");
	}
}