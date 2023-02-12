package io.github.akjo03.discord.cscbot.services.rules;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RulesMessageProvider {
	private final BotConfigService botConfigService;

	public MessageCreateData getRulesMessageCreateData(Languages language) {
		return botConfigService.getMessage("RULES_MESSAGE", language).toMessageCreateData();
	}

	public MessageEditData getRulesMessageEditData(Languages language) {
		return botConfigService.getMessage("RULES_MESSAGE", language).toMessageEditData();
	}
}