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
	@JsonProperty("localized_messages")
	private List<CscBotLocalizedMessage> localizedMessages = new ArrayList<>();

	@JsonSerialize
	@JsonDeserialize
	@JsonProperty("paginated_messages")
	private List<CscBotPaginatedMessage> paginatedMessages = new ArrayList<>();

	@JsonCreator
	public CscBotData(
		@JsonProperty("localized_messages") List<CscBotLocalizedMessage> localizedMessages,
		@JsonProperty("paginated_messages") List<CscBotPaginatedMessage> paginatedMessages
	) {
		this.localizedMessages = localizedMessages;
		this.paginatedMessages = paginatedMessages;
	}
}