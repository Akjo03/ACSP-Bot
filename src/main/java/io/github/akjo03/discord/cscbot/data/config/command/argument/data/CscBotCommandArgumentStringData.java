package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import lombok.*;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotCommandArgumentStringData implements CscBotCommandArgumentData<String> {
	@JsonSerialize
	@JsonDeserialize
	private int minLength;

	@JsonSerialize
	@JsonDeserialize
	private int maxLength;

	@JsonSerialize
	@JsonDeserialize
	private String defaultValue;

	@JsonCreator
	public CscBotCommandArgumentStringData(
			@JsonProperty("min_length") int minLength,
			@JsonProperty("max_length") int maxLength,
			@JsonProperty("default") String defaultValue
	) {
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.defaultValue = defaultValue;
	}

	@Override
	public Optional<CscBotConfigMessage> validate(String value, ErrorMessageService errorMessageService) {
		// TODO: Add labels for validation error messages
		if (value == null) {
			return Optional.of(errorMessageService.getErrorMessage(
					"",
					"",
					List.of(),
					List.of(),
					Optional.empty()
			));
		}
		if (value.length() < minLength) {
			return Optional.of(errorMessageService.getErrorMessage(
					"",
					"",
					List.of(),
					List.of(),
					Optional.empty()
			));
		}
		if (value.length() > maxLength) {
			return Optional.of(errorMessageService.getErrorMessage(
					"",
					"",
					List.of(),
					List.of(),
					Optional.empty()
			));
		}

		return Optional.empty();
	}
}