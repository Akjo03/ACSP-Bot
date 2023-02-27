package io.github.akjo03.discord.cscbot.data.config.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.Accessors;
import net.dv8tion.jda.api.entities.MessageEmbed;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@SuppressWarnings("unused")
public class CscBotConfigMessageEmbedField {
	@JsonSerialize
	@JsonDeserialize
	private String name;

	@JsonSerialize
	@JsonDeserialize
	private String value;

	@JsonSerialize
	@JsonDeserialize
	private boolean inline;

	@JsonCreator
	public CscBotConfigMessageEmbedField(
			@JsonProperty("name") String name,
			@JsonProperty("value") String value,
			@JsonProperty("inline") boolean inline
	) {
		this.name = name;
		this.value = value;
		this.inline = inline;
	}

	public boolean equalsToEmbedField(MessageEmbed.Field field) {
		if (field == null) {
			return false;
		}

		if (name != null && !name.equals(field.getName())) {
			return false;
		}

		if (value != null && !value.equals(field.getValue())) {
			return false;
		}

		if (inline != field.isInline()) {
			return false;
		}

		return true;
	}
}