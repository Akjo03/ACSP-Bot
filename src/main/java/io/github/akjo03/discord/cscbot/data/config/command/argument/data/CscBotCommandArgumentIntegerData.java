package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotCommandArgumentIntegerData {
	@JsonSerialize
	@JsonDeserialize
	private int min;

	@JsonSerialize
	@JsonDeserialize
	private int max;

	@JsonSerialize
	@JsonDeserialize
	private int defaultValue;

	@JsonCreator
	public CscBotCommandArgumentIntegerData(
			@JsonProperty("min") int min,
			@JsonProperty("max") int max,
			@JsonProperty("default") int defaultValue
	) {
		this.min = min;
		this.max = max;
		this.defaultValue = defaultValue;
	}
}