package io.github.akjo03.discord.cscbot.services.welcome;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class WelcomeMessageProvider {
	private final BotConfigService botConfigService;

	public MessageCreateData getWelcomeMessageCreateData(Languages language) {
		CscBotConfigMessage configMessage = botConfigService.getMessage("WELCOME_MESSAGE", language);
		if (configMessage == null) {
			return null;
		}
		return configMessage.toMessageCreateData();
	}

	public MessageEditData getWelcomeMessageEditData(Languages language) {
		CscBotConfigMessage configMessage = botConfigService.getMessage("WELCOME_MESSAGE", language);
		if (configMessage == null) {
			return null;
		}
		return configMessage.toMessageEditData();
	}
}