package io.github.akjo03.discord.cscbot.data.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageWrapper;
import io.github.akjo03.discord.cscbot.data.config.string.CscBotConfigString;
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
	private List<CscBotConfigString> strings;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommand> commands;

	@JsonSerialize
	@JsonDeserialize
	private String commandPrefix;

	@JsonCreator
	public CscBotConfig(
			@JsonProperty("messages") List<CscBotConfigMessageWrapper> messages,
			@JsonProperty("strings") List<CscBotConfigString> strings,
			@JsonProperty("commands") List<CscBotCommand> commands,
			@JsonProperty("command_prefix") String commandPrefix
	) {
		this.messages = messages;
		this.strings = strings;
		this.commands = commands;
		this.commandPrefix = commandPrefix;
	}
}