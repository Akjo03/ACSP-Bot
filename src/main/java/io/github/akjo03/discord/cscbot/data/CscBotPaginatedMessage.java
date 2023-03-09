package io.github.akjo03.discord.cscbot.data;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "paginated_messages")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotPaginatedMessage {
	@Id
	@EqualsAndHashCode.Include
	private String id;

	private String messageId;

	private String label;

	private Integer page;

	public CscBotPaginatedMessage(String messageId, String label, Integer page) {
		this.messageId = messageId;
		this.label = label;
		this.page = page;
	}

	@Setter
	@Accessors(chain = true)
	public static class Builder {
		private String messageId;
		private String label;
		private Integer page;

		public CscBotPaginatedMessage build() {
			if (messageId == null) {
				throw new IllegalStateException("ID for CscBotPaginatedMessage is not set!");
			}
			if (label == null) {
				throw new IllegalStateException("Label for CscBotPaginatedMessage is not set!");
			}
			if (page == null) {
				throw new IllegalStateException("Page for CscBotPaginatedMessage is not set!");
			}
			return new CscBotPaginatedMessage(messageId, label, page);
		}

		public static CscBotPaginatedMessage.Builder create() {
			return new CscBotPaginatedMessage.Builder();
		}
	}
}