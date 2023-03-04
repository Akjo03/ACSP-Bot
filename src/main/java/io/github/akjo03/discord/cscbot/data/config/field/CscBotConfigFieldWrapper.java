package io.github.akjo03.discord.cscbot.data.config.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageEmbedField;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotConfigFieldWrapper {
	@JsonSerialize
	@JsonDeserialize
	private String label;

	@JsonSerialize
	@JsonDeserialize
	private String language;

	@JsonSerialize
	@JsonDeserialize
	private CscBotConfigMessageEmbedField field;

	@JsonCreator
	public CscBotConfigFieldWrapper(
			@JsonProperty("label") String label,
			@JsonProperty("language") String language,
			@JsonProperty("message") CscBotConfigMessageEmbedField field
	) {
		this.label = label;
		this.language = language;
		this.field = field;
	}
}