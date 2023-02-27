package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.CscBot;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentValidationException;
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

	public CscBotConfigMessage getCommandArgumentValidationErrorMessage(List<CscCommandArgumentValidationException> exceptions, Optional<Languages> language) {
		// TODO: Merge all exceptions into one message
		return exceptions != null && !exceptions.isEmpty() ? exceptions.get(0).getErrorMessage() : getErrorMessage(
				"errors.unknown.title",
				"errors.unknown.description",
				List.of(),
				List.of(),
				language
		);
	}
}