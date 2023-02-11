package io.github.akjo03.discord.cscbot.data.config.string;

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
public class CscBotConfigString {
	@JsonSerialize
	@JsonDeserialize
	private String label;

	@JsonSerialize
	@JsonDeserialize
	private String language;

	@JsonSerialize
	@JsonDeserialize
	private String value;

	@JsonCreator
	public CscBotConfigString(
			@JsonProperty("label") String label,
			@JsonProperty("language") String language,
			@JsonProperty("value") String value
	) {
		this.label = label;
		this.language = language;
		this.value = value;
	}
}