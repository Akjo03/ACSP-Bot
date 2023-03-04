package io.github.akjo03.discord.cscbot.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotData {
	@JsonSerialize
	@JsonDeserialize
	private List<CscBotLocalizedMessage> localizedMessages = new ArrayList<>();

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotPaginatedMessage> paginatedMessages = new ArrayList<>();

	@JsonCreator
	public CscBotData(
		@JsonProperty("localized_messages") List<CscBotLocalizedMessage> messages
	) {
		this.localizedMessages = messages;
	}
}