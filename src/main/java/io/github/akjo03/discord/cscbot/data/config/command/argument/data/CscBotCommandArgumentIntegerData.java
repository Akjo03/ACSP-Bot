package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.command.argument.conversion.CscCommandArgumentConverterProvider;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentParseException;
import io.github.akjo03.lib.result.Result;
import lombok.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotCommandArgumentIntegerData implements CscBotCommandArgumentData<Integer> {
	@JsonSerialize
	@JsonDeserialize
	private int min;

	@JsonSerialize
	@JsonDeserialize
	private int max;

	@JsonSerialize
	@JsonDeserialize
	private Integer defaultValue;

	@JsonCreator
	public CscBotCommandArgumentIntegerData(
			@JsonProperty("min") int min,
			@JsonProperty("max") int max,
			@JsonProperty("default") Integer defaultValue
	) {
		this.min = min;
		this.max = max;
		this.defaultValue = defaultValue;
	}

	@Override
	public Result<Integer> parse(String commandName, String argumentName, String value, MessageReceivedEvent event, BotConfigService botConfigService, StringsResourceService stringsResourceService) {
		if ((value == null || value.isEmpty())) {
			if (defaultValue != null) {
				return Result.success(defaultValue);
			}

			return Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
					"errors.command_argument_parsing_report.fields.reason.no_value",
					List.of(),
					null, botConfigService
			));
		}

		Integer parsedValue = Result.from(() -> CscCommandArgumentConverterProvider.INTEGER.provide().convertForward(value)).getOrNull();
		if (parsedValue == null) {
			return Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
					"errors.command_argument_parsing_report.fields.reason.invalid_type",
					List.of(
							stringsResourceService.getString("command.arguments.type.integer", Optional.empty()),
							event.getGuild().getId(),
							event.getChannel().getId(),
							stringsResourceService.getString("command.arguments.type.integer.tooltip", Optional.empty())
					),
					null, botConfigService
			));
		}

		if (parsedValue < min) {
			return Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
					"errors.command_argument_parsing_report.fields.reason.integer.too_small",
					List.of(
							String.valueOf(min)
					),
					null, botConfigService
			));
		}
		if (parsedValue > max) {
			return Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
					"errors.command_argument_parsing_report.fields.reason.integer.too_big",
					List.of(
							String.valueOf(max)
					),
					null, botConfigService
			));
		}

		return Result.success(parsedValue);
	}
}