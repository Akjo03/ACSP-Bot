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
	private List<CscBotMessage> messages = new ArrayList<>();

	@JsonCreator
	public CscBotData(
		@JsonProperty("messages") List<CscBotMessage> messages
	) {
		this.messages = messages;
	}
}