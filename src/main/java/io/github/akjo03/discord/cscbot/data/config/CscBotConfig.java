package io.github.akjo03.discord.cscbot.data.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.field.CscBotConfigFieldWrapper;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageWrapper;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotConfig {
	@JsonSerialize
	@JsonDeserialize
	private List<CscBotConfigMessageWrapper> messages;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotConfigFieldWrapper> fields;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommand> commands;

	@JsonSerialize
	@JsonDeserialize
	private String commandPrefix;

	@JsonCreator
	public CscBotConfig(
			@JsonProperty("messages") List<CscBotConfigMessageWrapper> messages,
			@JsonProperty("fields") List<CscBotConfigFieldWrapper> fields,
			@JsonProperty("commands") List<CscBotCommand> commands,
			@JsonProperty("command_prefix") String commandPrefix
	) {
		this.messages = messages;
		this.fields = fields;
		this.commands = commands;
		this.commandPrefix = commandPrefix;
	}
}