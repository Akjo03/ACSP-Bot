package io.github.akjo03.discord.cscbot.data.config.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotConfigInteractionButtonComponent implements CscBotConfigComponent {
	@JsonSerialize
	@JsonDeserialize
	private String interactionId;

	@JsonSerialize
	@JsonDeserialize
	private String style;

	@JsonSerialize
	@JsonDeserialize
	private String text;

	@JsonSerialize
	@JsonDeserialize
	private CscBotConfigButtonComponentEmoji emoji;

	@JsonSerialize
	@JsonDeserialize
	private boolean disabled;

	@JsonCreator
	public CscBotConfigInteractionButtonComponent(
			@JsonProperty("interaction_id") String interactionId,
			@JsonProperty("style") String style,
			@JsonProperty("text") String text,
			@JsonProperty("emoji") CscBotConfigButtonComponentEmoji emoji,
			@JsonProperty("disabled") boolean disabled
	) {
		this.interactionId = interactionId;
		this.style = style;
		this.text = text;
		this.emoji = emoji;
		this.disabled = disabled;
	}
}