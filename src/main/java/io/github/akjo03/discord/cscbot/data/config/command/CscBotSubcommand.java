package io.github.akjo03.discord.cscbot.data.config.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.command.argument.CscBotCommandArgument;
import io.github.akjo03.discord.cscbot.data.config.command.permission.CscBotCommandPermission;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotSubcommand {
	@JsonSerialize
	@JsonDeserialize
	private String name;

	@JsonSerialize
	@JsonDeserialize
	private boolean available;

	@JsonSerialize
	@JsonDeserialize
	private String description;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommandArgument<?>> arguments;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommandPermission> permissions;

	@JsonCreator
	public CscBotSubcommand(
			@JsonProperty("name") String name,
			@JsonProperty("available") boolean available,
			@JsonProperty("description") String description,
			@JsonProperty("arguments") List<CscBotCommandArgument<?>> arguments,
			@JsonProperty("permissions") List<CscBotCommandPermission> permissions
	) {
		this.name = name;
		this.available = available;
		this.description = description;
		this.arguments = arguments;
		this.permissions = permissions;
	}
}