package io.github.akjo03.discord.cscbot.data.config.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotConfigMessageEmbedFooter {
	@JsonSerialize
	@JsonDeserialize
	private String text;

	@JsonSerialize
	@JsonDeserialize
	private String timestamp;

	@JsonSerialize
	@JsonDeserialize
	private String iconURL;

	@JsonCreator
	public CscBotConfigMessageEmbedFooter(
			@JsonProperty("text") String text,
			@JsonProperty("timestamp") String timestamp,
			@JsonProperty("icon_url") String iconURL
	) {
		this.text = text;
		this.timestamp = timestamp;
		this.iconURL = iconURL;
	}

	public boolean equalsToEmbedFooter(MessageEmbed.Footer footer) {
		if (footer == null) {
			return false;
		}

		if (text != null && !text.equals(footer.getText())) {
			return false;
		}

		if (iconURL != null && !iconURL.equals(footer.getIconUrl())) {
			return false;
		}

		return true;
	}
}