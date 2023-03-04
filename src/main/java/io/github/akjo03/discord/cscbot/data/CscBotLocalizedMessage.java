package io.github.akjo03.discord.cscbot.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.constants.Languages;
import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotLocalizedMessage {
	@JsonSerialize
	@JsonDeserialize
	@EqualsAndHashCode.Include
	private String id;

	@JsonSerialize
	@JsonDeserialize
	private String label;

	@JsonSerialize
	@JsonDeserialize
	private String language;

	@JsonCreator
	public CscBotLocalizedMessage(
		@JsonProperty("id") String id,
		@JsonProperty("label") String label,
		@JsonProperty("language") String language
	) {
		this.id = id;
		this.label = label;
		this.language = language;
	}

	@Setter
	@Accessors(chain = true)
	public static class Builder {
		private String id;
		private String label;
		private Languages language;

		public CscBotLocalizedMessage build() {
			if (id == null) {
				throw new IllegalStateException("ID for CscBotMessage is not set!");
			}
			if (label == null) {
				throw new IllegalStateException("Label for CscBotMessage is not set!");
			}
			if (language == null) {
				throw new IllegalStateException("Language for CscBotMessage is not set!");
			}
			return new CscBotLocalizedMessage(id, label, language.toString());
		}

		public static Builder create() {
			return new Builder();
		}
	}
}