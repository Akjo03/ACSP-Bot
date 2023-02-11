package io.github.akjo03.discord.cscbot.data.config.message;

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
public class CscBotConfigMessageWrapper {
	@JsonSerialize
	@JsonDeserialize
	private String label;

	@JsonSerialize
	@JsonDeserialize
	private String language;

	@JsonSerialize
	@JsonDeserialize
	private CscBotConfigMessage message;

	@JsonCreator
	public CscBotConfigMessageWrapper(
			@JsonProperty("label") String label,
			@JsonProperty("language") String language,
			@JsonProperty("message") CscBotConfigMessage message
	) {
		this.label = label;
		this.language = language;
		this.message = message;
	}
}