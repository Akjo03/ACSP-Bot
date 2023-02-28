package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.command.argument.conversion.CscCommandArgumentConverterProvider;
import io.github.akjo03.discord.cscbot.util.exception.CscCommandArgumentParseException;
import io.github.akjo03.lib.result.Result;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Optional;

public abstract class CscBotCommandArgumentData<T> {
	public abstract T getDefaultValue();
	public abstract Result<T> parse(String commandName, String argumentName, String value, MessageReceivedEvent event, BotConfigService botConfigService, StringsResourceService stringsResourceService);

	protected Result<T> checkForNull(String commandName, String argumentName, String value, BotConfigService botConfigService) {
		if (value == null || value.isEmpty()) {
			if (getDefaultValue() != null) {
				return Result.success(getDefaultValue());
			}

			return Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
					"errors.command_argument_parsing_report.fields.reason.no_value",
					List.of(),
					null, botConfigService
			));
		}

		return null;
	}

	protected Result<T> getParsedValue(String commandName, String argumentName, String value, MessageReceivedEvent event, BotConfigService botConfigService, StringsResourceService stringsResourceService, CscCommandArgumentConverterProvider<T, ?> converterType, CscCommandArgumentTypes type) {
		T parsedValue = Result.from(() -> converterType.provide().convertForward(value)).getOrNull();
		if (parsedValue == null) {
			return Result.fail(new CscCommandArgumentParseException(commandName, argumentName,
					"errors.command_argument_parsing_report.fields.reason.invalid_type",
					List.of(
							stringsResourceService.getString(type.getKey(), Optional.empty()),
							event.getGuild().getId(),
							event.getChannel().getId(),
							stringsResourceService.getString(type.getTooltipKey(), Optional.empty())
					),
					null, botConfigService
			));
		}

		return Result.success(parsedValue);
	}
}