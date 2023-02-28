package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.command.argument.conversion.CscCommandArgumentConverterProvider;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentParseException;
import io.github.akjo03.lib.math.Range;
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
public class CscBotCommandArgumentStringData implements CscBotCommandArgumentData<String> {
	@JsonSerialize
	@JsonDeserialize
	private int minLength;

	@JsonSerialize
	@JsonDeserialize
	private int maxLength;

	@JsonSerialize
	@JsonDeserialize
	private String defaultValue;

	@JsonCreator
	public CscBotCommandArgumentStringData(
			@JsonProperty("min_length") int minLength,
			@JsonProperty("max_length") int maxLength,
			@JsonProperty("default") String defaultValue
	) {
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.defaultValue = defaultValue;
	}

	@Override
	public Result<String> parse(String commandName, String argumentName, String value, MessageReceivedEvent event, BotConfigService botConfigService, StringsResourceService stringsResourceService) {
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

		String parsedValue = Result.from(() -> CscCommandArgumentConverterProvider.STRING.provide().convertForward(value)).getOrNull();
		if (parsedValue == null) {
			return Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
					"errors.command_argument_parsing_report.fields.reason.invalid_type",
					List.of(
							stringsResourceService.getString("command.arguments.type.string", Optional.empty()),
							event.getGuild().getId(),
							event.getChannel().getId(),
							stringsResourceService.getString("command.arguments.type.string.tooltip", Optional.empty())
					),
					null, botConfigService
			));
		}

		Range<Integer> validLengthRange = new Range<>(minLength, maxLength);
		return validLengthRange.checkRange(
				parsedValue.length(),
				Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
						"errors.command_argument_parsing_report.fields.reason.string.too_short",
						List.of(
								String.valueOf(minLength)
						),
						null, botConfigService
				)),
				Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
						"errors.command_argument_parsing_report.fields.reason.string.too_long",
						List.of(
								String.valueOf(maxLength)
						),
						null, botConfigService
				)),
				Result.success(parsedValue)
		);
	}
}