package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.lib.result.Result;
import lombok.*;

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
	public Result<Integer> parse(String value, ErrorMessageService errorMessageService) {
		return Result.success(Integer.parseInt(value));
	}
}