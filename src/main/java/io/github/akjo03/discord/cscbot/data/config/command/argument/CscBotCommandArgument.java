package io.github.akjo03.discord.cscbot.data.config.command.argument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotCommandArgument {
	@JsonSerialize
	@JsonDeserialize
	@EqualsAndHashCode.Include
	private String name;

	@JsonSerialize
	@JsonDeserialize
	private String type;

	@JsonSerialize
	@JsonDeserialize
	private String description;

	@JsonSerialize
	@JsonDeserialize
	private boolean required;

	@JsonSerialize
	@JsonDeserialize
	private JsonNode data;

	@JsonCreator
	public CscBotCommandArgument(
			@JsonProperty("name") String name,
			@JsonProperty("type") String type,
			@JsonProperty("description") String description,
			@JsonProperty("required") boolean required,
			@JsonProperty("data") JsonNode data
	) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.required = required;
		this.data = data;
	}
}
