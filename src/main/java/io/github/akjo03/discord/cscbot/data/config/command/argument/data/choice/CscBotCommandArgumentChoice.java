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
public class CscBotCommandArgumentChoice {
	@JsonSerialize
	@JsonDeserialize
	private List<String> names;

	@JsonSerialize
	@JsonDeserialize
	private boolean caseSensitive;

	@JsonSerialize
	@JsonDeserialize
	private String value;

	@JsonCreator
	public CscBotCommandArgumentChoice(
			@JsonProperty("names") List<String> names,
			@JsonProperty("case_sensitive") boolean caseSensitive,
			@JsonProperty("value") String value
	) {
		this.names = names;
		this.caseSensitive = caseSensitive;
		this.value = value;
	}

	public String getValue(String name) {
		if (names.stream().anyMatch(n -> caseSensitive ? n.equals(name) : n.equalsIgnoreCase(name))) {
			return value;
		}
		return null;
	}
}