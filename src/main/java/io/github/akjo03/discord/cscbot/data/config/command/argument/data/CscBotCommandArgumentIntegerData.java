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
public class CscBotCommandArgumentIntegerData extends CscBotCommandArgumentData<Integer> {
	@JsonSerialize
	@JsonDeserialize
	private Integer min;

	@JsonSerialize
	@JsonDeserialize
	private Integer max;

	@JsonSerialize
	@JsonDeserialize
	private Integer defaultValue;

	@JsonCreator
	public CscBotCommandArgumentIntegerData(
			@JsonProperty("min") Integer min,
			@JsonProperty("max") Integer max,
			@JsonProperty("default") Integer defaultValue
	) {
		this.min = min;
		this.max = max;
		this.defaultValue = defaultValue;
	}

	@Override
	public Result<Integer> parse(String commandName, String argumentName, String value, MessageReceivedEvent event, BotConfigService botConfigService, StringsResourceService stringsResourceService) {
		Result<Integer> nullCheck = checkForNull(commandName, argumentName, value, botConfigService);
		if (nullCheck != null) {
			return nullCheck;
		}

		Result<Integer> parsedValueResult = getParsedValue(commandName, argumentName, value, event, botConfigService, stringsResourceService, CscCommandArgumentConverterProvider.INTEGER, CscCommandArgumentTypes.INTEGER);
		if (parsedValueResult.isError()) {
			return parsedValueResult;
		}
		Integer parsedValue = parsedValueResult.get();

		Range<Integer> validRange = new Range<>(min, max);
		return validRange.checkRange(
				parsedValue,
				() -> Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
						"errors.command_argument_parsing_report.fields.reason.integer.too_small",
						List.of(
								String.valueOf(min)
						),
						null, botConfigService
				)),
				() -> Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
						"errors.command_argument_parsing_report.fields.reason.integer.too_big",
						List.of(
								String.valueOf(max)
						),
						null, botConfigService
				)),
				Result.success(parsedValue)
		);
	}
}