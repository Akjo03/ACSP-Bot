package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.command.argument.conversion.CscCommandArgumentConverterProvider;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentParseException;
import io.github.akjo03.lib.math.Range;
import io.github.akjo03.lib.result.Result;
import lombok.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings({"unused", "DuplicatedCode"})
public class CscBotCommandArgumentStringData extends CscBotCommandArgumentData<String> {
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
		Result<String> nullCheck = checkForNull(commandName, argumentName, value, botConfigService);
		if (nullCheck != null) {
			return nullCheck;
		}

		Result<String> parsedValueResult = getParsedValue(commandName, argumentName, value, event, botConfigService, stringsResourceService, CscCommandArgumentConverterProvider.STRING, CscCommandArgumentTypes.STRING);
		if (parsedValueResult.isError()) {
			return parsedValueResult;
		}
		String parsedValue = parsedValueResult.get();

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