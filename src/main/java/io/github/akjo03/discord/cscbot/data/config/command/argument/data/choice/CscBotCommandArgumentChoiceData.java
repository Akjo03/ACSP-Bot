package io.github.akjo03.discord.cscbot.data.config.command.argument.data.choice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotCommandArgumentChoiceData {
	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommandArgumentChoice> choices;

	@JsonSerialize
	@JsonDeserialize
	private boolean exclusive;

	@JsonSerialize
	@JsonDeserialize
	private String defaultChoice;

	@JsonCreator
	public CscBotCommandArgumentChoiceData(
			@JsonProperty("choices") List<CscBotCommandArgumentChoice> choices,
			@JsonProperty("choice_exclusive") boolean exclusive,
			@JsonProperty("default") String defaultChoice
	) {
		this.choices = choices;
		this.exclusive = exclusive;
		this.defaultChoice = defaultChoice;
	}
}