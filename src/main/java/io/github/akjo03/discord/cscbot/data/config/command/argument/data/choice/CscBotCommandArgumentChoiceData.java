package io.github.akjo03.discord.cscbot.data.config.command.argument.data.choice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
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
	public Optional<CscBotConfigMessage> validate(String value, ErrorMessageService errorMessageService) {
		if (value == null) {
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