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

import java.util.Optional;

@Component
@RequiredArgsConstructor
@EnableLogger
public class RulesMessageProvider {
	private Logger logger;

	private final BotConfigService botConfigService;

	public MessageCreateData getRulesMessageCreateData(Optional<Languages> language) {
		CscBotConfigMessage message = botConfigService.getMessage("RULES_MESSAGE", language);
		if (message == null) {
			logger.error("Failed to get rules message!");
			return null;
		}
		return message.toMessageCreateData();
	}

	public MessageEditData getRulesMessageEditData(Optional<Languages> language) {
		CscBotConfigMessage message = botConfigService.getMessage("RULES_MESSAGE", language);
		if (message == null) {
			logger.error("Failed to get rules message!");
			return null;
		}
		return message.toMessageEditData();
	}
}