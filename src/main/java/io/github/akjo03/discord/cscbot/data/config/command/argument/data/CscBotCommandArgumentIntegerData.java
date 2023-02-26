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
public class CscBotCommandArgumentIntegerData implements CscBotCommandArgumentData<Integer> {
	@JsonSerialize
	@JsonDeserialize
	private int min;

	@JsonSerialize
	@JsonDeserialize
	private int max;

	@JsonSerialize
	@JsonDeserialize
	private Integer defaultValue;

	@JsonCreator
	public CscBotCommandArgumentIntegerData(
			@JsonProperty("min") int min,
			@JsonProperty("max") int max,
			@JsonProperty("default") Integer defaultValue
	) {
		this.min = min;
		this.max = max;
		this.defaultValue = defaultValue;
	}

	@Override
	public Result<Void> validate(Integer value, ErrorMessageService errorMessageService) {
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
		if (value < min) {
			return Result.fail(new CscCommandArgumentValidationException(
					"",
					"",
					List.of(),
					List.of(),
					null,
					errorMessageService
			));
		}
		if (value > max) {
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