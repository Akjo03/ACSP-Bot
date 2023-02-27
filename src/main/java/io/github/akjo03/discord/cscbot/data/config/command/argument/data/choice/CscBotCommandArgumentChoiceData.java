package io.github.akjo03.discord.cscbot.data.config.command.argument.data.choice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import io.github.akjo03.discord.cscbot.util.command.argument.conversion.CscCommandArgumentConverterProvider;
import io.github.akjo03.lib.result.Result;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotCommandArgumentChoiceData implements CscBotCommandArgumentData<String> {
	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommandArgumentChoice> choices;

	@JsonSerialize
	@JsonDeserialize
	private boolean exclusive;

	@JsonSerialize
	@JsonDeserialize
	private String defaultValue;

	@JsonCreator
	public CscBotCommandArgumentChoiceData(
			@JsonProperty("choices") List<CscBotCommandArgumentChoice> choices,
			@JsonProperty("choice_exclusive") boolean exclusive,
			@JsonProperty("default") String defaultValue
	) {
		this.choices = choices;
		this.exclusive = exclusive;
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