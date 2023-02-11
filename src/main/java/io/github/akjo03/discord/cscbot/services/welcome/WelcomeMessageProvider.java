package io.github.akjo03.discord.cscbot.services.welcome;

import io.github.akjo03.discord.cscbot.constants.Languages;
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
		return botConfigService.getMessage("WELCOME_MESSAGE", language).toMessageCreateData();
	}

	public MessageEditData getWelcomeMessageEditData(Languages language) {
		return botConfigService.getMessage("WELCOME_MESSAGE", language).toMessageEditData();
	}
}