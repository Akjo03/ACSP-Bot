package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentValidationException;
import io.github.akjo03.lib.result.Result;
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
	public Result<Void> validate(String value, ErrorMessageService errorMessageService) {
		// TODO: Add labels for validation error messages
		if (value == null) {
			return Result.fail(new CscCommandArgumentValidationException(
					"",
					"",
					List.of(),
					List.of(),
					null,
					errorMessageService
			));
		}
		if (value.length() < minLength) {
			return Result.fail(new CscCommandArgumentValidationException(
					"",
					"",
					List.of(),
					List.of(),
					null,
					errorMessageService
			));
		}
		if (value.length() > maxLength) {
			return Result.fail(new CscCommandArgumentValidationException(
					"",
					"",
					List.of(),
					List.of(),
					null,
					errorMessageService
			));
		}

		return Result.empty();
	}
}