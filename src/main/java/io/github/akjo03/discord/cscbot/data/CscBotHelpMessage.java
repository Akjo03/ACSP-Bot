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
public class CscBotHelpMessage {
	@JsonSerialize
	@JsonDeserialize
	@EqualsAndHashCode.Include
	private String id;

	@JsonSerialize
	@JsonDeserialize
	private String path;

	@JsonCreator
	public CscBotHelpMessage(
		@JsonProperty("id") String id,
		@JsonProperty("path") String path
	) {
		this.id = id;
		this.path = path;
	}

	@Setter
	@Accessors(chain = true)
	public static class Builder {
		private String id;
		private String path;

		public CscBotHelpMessage build() {
			if (id == null) {
				throw new IllegalStateException("ID for CscBotHelpMessage is not set!");
			}
			if (path == null) {
				throw new IllegalStateException("Path for CscBotHelpMessage is not set!");
			}
			return new CscBotHelpMessage(id, path);
		}

		public static CscBotHelpMessage.Builder create() {
			return new CscBotHelpMessage.Builder();
		}
	}
}