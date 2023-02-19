package io.github.akjo03.discord.cscbot.services.rules;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableLogger
public class RulesMessageProvider {
	private Logger logger;

	private final BotConfigService botConfigService;

	public MessageCreateData getRulesMessageCreateData(Languages language) {
		CscBotConfigMessage message = botConfigService.getMessage("RULES_MESSAGE", language);
		if (message == null) {
			logger.error("Failed to get rules message!");
			return null;
		}
		return message.toMessageCreateData();
	}

	public MessageEditData getRulesMessageEditData(Languages language) {
		CscBotConfigMessage message = botConfigService.getMessage("RULES_MESSAGE", language);
		if (message == null) {
			logger.error("Failed to get rules message!");
			return null;
		}
		return message.toMessageEditData();
	}
}