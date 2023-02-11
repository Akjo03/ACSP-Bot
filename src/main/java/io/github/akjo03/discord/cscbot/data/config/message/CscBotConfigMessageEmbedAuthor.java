package io.github.akjo03.discord.cscbot.data.config.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import net.dv8tion.jda.api.entities.MessageEmbed;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotConfigMessageEmbedAuthor {
	@JsonSerialize
	@JsonDeserialize
	private String name;

	@JsonSerialize
	@JsonDeserialize
	private String url;

	@JsonSerialize
	@JsonDeserialize
	private String iconURL;

	@JsonCreator
	public CscBotConfigMessageEmbedAuthor(
			@JsonProperty("name") String name,
			@JsonProperty("url") String url,
			@JsonProperty("icon_url") String iconURL
	) {
		this.name = name;
		this.url = url;
		this.iconURL = iconURL;
	}

	public boolean equalsToEmbedAuthor(MessageEmbed.AuthorInfo author) {
		if (author == null) {
			return false;
		}

		if (name != null && !name.equals(author.getName())) {
			return false;
		}

		if (url != null && !url.equals(author.getUrl())) {
			return false;
		}

		if (iconURL != null && !iconURL.equals(author.getIconUrl())) {
			return false;
		}

		return true;
	}
}