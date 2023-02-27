package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.util.command.argument.conversion.CscCommandArgumentConverterProvider;
import io.github.akjo03.lib.result.Result;
import lombok.*;

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
	public Result<String> parse(String value) {
		// TODO: Add proper error handling and validation
		if ((value == null || value.isEmpty())) {
			if (defaultValue != null) {
				return Result.success(defaultValue);
			}
		}
		return Result.success(CscCommandArgumentConverterProvider.STRING.provide().convertForward(value));
	}
}