package io.github.akjo03.discord.cscbot.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "help_messages")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotHelpMessage {
	@Id
	@EqualsAndHashCode.Include
	private String id;

	private String messageId;

	private String path;

	public CscBotHelpMessage(String messageId, String path) {
		this.messageId = messageId;
		this.path = path;
	}

	@Setter
	@Accessors(chain = true)
	public static class Builder {
		private String messageId;
		private String path;

		public CscBotHelpMessage build() {
			if (messageId == null) {
				throw new IllegalStateException("ID for CscBotHelpMessage is not set!");
			}
			if (path == null) {
				throw new IllegalStateException("Path for CscBotHelpMessage is not set!");
			}
			return new CscBotHelpMessage(messageId, path);
		}

		public static CscBotHelpMessage.Builder create() {
			return new CscBotHelpMessage.Builder();
		}
	}
}