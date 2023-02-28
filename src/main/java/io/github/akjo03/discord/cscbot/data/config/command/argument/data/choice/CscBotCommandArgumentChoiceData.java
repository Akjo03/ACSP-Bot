package io.github.akjo03.discord.cscbot.data.config.command.argument.data.choice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.command.argument.conversion.CscCommandArgumentConverterProvider;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentNullException;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentParseException;
import io.github.akjo03.lib.result.Result;
import lombok.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings({"unused", "DuplicatedCode"})
public class CscBotCommandArgumentChoiceData extends CscBotCommandArgumentData<String> {
	@JsonSerialize
	@JsonDeserialize
	private List<CscBotCommandArgumentChoice> choices;

	@JsonSerialize
	@JsonDeserialize
	private boolean exclusive;

	@JsonSerialize
	@JsonDeserialize
	private String defaultValue;

	@JsonCreator
	public CscBotCommandArgumentChoiceData(
			@JsonProperty("choices") List<CscBotCommandArgumentChoice> choices,
			@JsonProperty("choice_exclusive") boolean exclusive,
			@JsonProperty("default") String defaultValue
	) {
		this.choices = choices;
		this.exclusive = exclusive;
		this.defaultValue = defaultValue;
	}

	@Override
	public Result<String> parse(String commandName, String argumentName, String value, boolean required, MessageReceivedEvent event, BotConfigService botConfigService, StringsResourceService stringsResourceService) {
		Result<String> nullCheck = checkForNull(commandName, argumentName, value, required, botConfigService);
		if (nullCheck != null) {
			return nullCheck;
		}

		Result<String> parsedValueResult = getParsedValue(commandName, argumentName, value, event, botConfigService, stringsResourceService, CscCommandArgumentConverterProvider.STRING, CscCommandArgumentTypes.STRING);
		if (parsedValueResult.isError()) {
			return parsedValueResult;
		}
		String parsedValue = parsedValueResult.get();

		String choice = this.choices.stream()
				.map(choiceP -> choiceP.getValue(parsedValue))
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
		List<List<String>> availableChoices = this.choices.stream()
				.map(CscBotCommandArgumentChoice::getNames)
				.toList();
		String availableChoicesString = availableChoices.stream()
				.map(list -> list.stream()
						.map(s -> "`" + s + "`")
						.toList())
				.map(list -> "[" + String.join(", ", list) + "]")
				.toList()
				.stream()
				.reduce((s1, s2) -> s1 + ", " + s2)
				.orElse(null);
		if (availableChoicesString == null) {
			return Result.fail(new IllegalStateException("Available choices string is null!"));
		}

		if (choice == null && required) {
			return Result.fail(new CscCommandArgumentParseException(
					commandName, argumentName,
					"errors.command_argument_parsing_report.fields.reason.choice.invalid_option",
					List.of(
							stringsResourceService.getString(CscCommandArgumentTypes.CHOICE.getKey(), Optional.empty()),
							event.getGuild().getId(),
							event.getChannel().getId(),
							stringsResourceService.getString(CscCommandArgumentTypes.CHOICE.getTooltipKey(), Optional.empty(), commandName),
							availableChoicesString
					),
					null, botConfigService
			));
		}

		String choiceValue = this.choices.stream()
				.filter(choiceP -> choiceP.getValue(parsedValue) != null)
				.map(CscBotCommandArgumentChoice::getValue)
				.findFirst()
				.orElse(null);
		if (choiceValue == null) {
			return Result.fail(new CscCommandArgumentNullException());
		}

		return Result.success(choiceValue);
	}
}