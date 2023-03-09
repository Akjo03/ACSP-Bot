package io.github.akjo03.discord.cscbot.data;

import io.github.akjo03.discord.cscbot.constants.Languages;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "localized_messages")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotLocalizedMessage {
	@Id
	@EqualsAndHashCode.Include
	private String id;

	private String messageId;

	private String label;

	private String language;

	public CscBotLocalizedMessage(String messageId, String label, String language) {
		this.messageId = messageId;
		this.label = label;
		this.language = language;
	}

	@Setter
	@Accessors(chain = true)
	public static class Builder {
		private String messageId;
		private String label;
		private Languages language;

		public CscBotLocalizedMessage build() {
			if (messageId == null) {
				throw new IllegalStateException("ID for CscBotMessage is not set!");
			}
			if (label == null) {
				throw new IllegalStateException("Label for CscBotMessage is not set!");
			}
			if (language == null) {
				throw new IllegalStateException("Language for CscBotMessage is not set!");
			}
			return new CscBotLocalizedMessage(messageId, label, language.toString());
		}

		public static Builder create() {
			return new Builder();
		}
	}
}