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
public class CscBotCommand {
	@JsonSerialize
	@JsonDeserialize
	@EqualsAndHashCode.Include
	private String command;

	@JsonSerialize
	@JsonDeserialize
	private CscBotSubcommands subcommands;

	@JsonSerialize
	@JsonDeserialize
	private String description;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommandArgument> arguments;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommandPermission> permissions;

	@JsonCreator
	public CscBotCommand(
			@JsonProperty("command") String command,
			@JsonProperty("subcommands") CscBotSubcommands subcommands,
			@JsonProperty("description") String description,
			@JsonProperty("arguments") List<CscBotCommandArgument> arguments,
			@JsonProperty("permissions") List<CscBotCommandPermission> permissions
	) {
		this.command = command;
		this.subcommands = subcommands;
		this.description = description;
		this.arguments = arguments;
		this.permissions = permissions;
	}

	public static CscBotCommand copy(CscBotCommand command) {
		return new CscBotCommand(
				command.getCommand(),
				command.getSubcommands(),
				command.getDescription(),
				command.getArguments(),
				command.getPermissions()
		);
	}
}