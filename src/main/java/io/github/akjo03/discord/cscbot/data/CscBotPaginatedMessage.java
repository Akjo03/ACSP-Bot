package io.github.akjo03.discord.cscbot.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotPaginatedMessage {
	@JsonSerialize
	@JsonDeserialize
	@EqualsAndHashCode.Include
	private String id;

	@JsonSerialize
	@JsonDeserialize
	private String label;

	@JsonSerialize
	@JsonDeserialize
	private Integer page;

	@JsonCreator
	public CscBotPaginatedMessage(
		@JsonProperty("id") String id,
		@JsonProperty("label") String label,
		@JsonProperty("page") Integer page
	) {
		this.id = id;
		this.label = label;
		this.page = page;
	}

	@Setter
	@Accessors(chain = true)
	public static class Builder {
		private String id;
		private String label;
		private Integer page;

		public CscBotPaginatedMessage build() {
			if (id == null) {
				throw new IllegalStateException("ID for CscBotPaginatedMessage is not set!");
			}
			if (label == null) {
				throw new IllegalStateException("Label for CscBotPaginatedMessage is not set!");
			}
			if (page == null) {
				throw new IllegalStateException("Page for CscBotPaginatedMessage is not set!");
			}
			return new CscBotPaginatedMessage(id, label, page);
		}

		public static CscBotPaginatedMessage.Builder create() {
			return new CscBotPaginatedMessage.Builder();
		}
	}
}