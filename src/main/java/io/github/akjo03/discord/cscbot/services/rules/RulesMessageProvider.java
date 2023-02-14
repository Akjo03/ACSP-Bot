package io.github.akjo03.discord.cscbot.services.rules;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RulesMessageProvider {
	private static final Logger LOGGER = LoggerManager.getLogger(RulesMessageProvider.class);

	private final BotConfigService botConfigService;

	public MessageCreateData getRulesMessageCreateData(Languages language) {
		CscBotConfigMessage message = botConfigService.getMessage("RULES_MESSAGE", language);
		if (message == null) {
			LOGGER.error("Failed to get rules message!");
			return null;
		}
		return message.toMessageCreateData();
	}

	public MessageEditData getRulesMessageEditData(Languages language) {
		CscBotConfigMessage message = botConfigService.getMessage("RULES_MESSAGE", language);
		if (message == null) {
			LOGGER.error("Failed to get rules message!");
			return null;
		}
		return message.toMessageEditData();
	}
}