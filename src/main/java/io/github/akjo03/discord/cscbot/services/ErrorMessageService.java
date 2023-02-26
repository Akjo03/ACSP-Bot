package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.CscBot;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ErrorMessageService {
	private final BotConfigService botConfigService;
	private final StringsResourceService stringsResourceService;

	public CscBotConfigMessage getErrorMessage(
			String titleLabel,
			String descriptionLabel,
			List<String> titlePlaceholders,
			List<String> descriptionPlaceholders,
			Optional<Languages> language
	) {
		return botConfigService.getMessage("ERROR_MESSAGE", language,
				stringsResourceService.getString(titleLabel, language, titlePlaceholders.toArray(String[]::new)),
				stringsResourceService.getString(descriptionLabel, language, descriptionPlaceholders.toArray(String[]::new)),
				CscBot.getBotName(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
						.withZone(ZoneId.of("Europe/Zurich"))
						.format(Instant.now())
		);
	}
}