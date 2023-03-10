package io.github.akjo03.discord.cscbot.data.config.command.argument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.lib.result.Result;
import lombok.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotCommandArgument<T> {
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
	private CscBotCommandArgumentData<T> data;

	@JsonCreator
	public CscBotCommandArgument(
			@JsonProperty("name") String name,
			@JsonProperty("type") String type,
			@JsonProperty("description") String description,
			@JsonProperty("required") boolean required,
			@JsonProperty("data") CscBotCommandArgumentData<T> data
	) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.required = required;
		this.data = data;
	}

	public Result<T> parse(String commandName, String argumentName, String value, boolean required, MessageReceivedEvent event, BotConfigService botConfigService, StringsResourceService stringsResourceService) {
		return data.parse(commandName, argumentName, value, required, event, botConfigService, stringsResourceService);
	}
}
