package io.github.akjo03.discord.cscbot.util.exception;

import io.github.akjo03.discord.cscbot.CscBot;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@Getter
public class CscCommandArgumentParseException extends CscException {
	private final String argumentName;
	private final String reasonLabel;
	private final List<String> reasonPlaceholders;

	public CscCommandArgumentParseException(
			String commandName,
			String argumentName,
			String reasonLabel,
			List<String> reasonPlaceholders,
			Languages language,
			BotConfigService botConfigService
	) {
		super(
				"ARGUMENT_PARSE_ERROR",
				List.of(
						commandName,
						CscBot.getBotName(),
						new DateTimeFormatterBuilder()
								.appendPattern("yyyy-MM-dd HH:mm")
								.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
								.toFormatter().withZone(ZoneId.of("Europe/Zurich"))
								.format(Instant.now())
				),
				language,
				botConfigService
		);
		this.argumentName = argumentName;
		this.reasonLabel = reasonLabel;
		this.reasonPlaceholders = reasonPlaceholders;
	}
}