package io.github.akjo03.discord.cscbot.services;

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

	public CscBotConfigMessage getErrorMessage(String titleLabel, String descriptionLabel, String source, Instant timestamp, Optional<Languages> language, List<String> titlePlaceholders, List<String> descriptionPlaceholders) {
		return botConfigService.getMessage("ERROR_MESSAGE", language.orElse(Languages.ENGLISH),
				botConfigService.getString(titleLabel, language.orElse(Languages.ENGLISH), titlePlaceholders.toArray(String[]::new)).getValue(),
				botConfigService.getString(descriptionLabel, language.orElse(Languages.ENGLISH), descriptionPlaceholders.toArray(String[]::new)).getValue(),
				source,
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
						.withZone(ZoneId.of("Europe/Zurich"))
						.format(timestamp)
		);
	}
}