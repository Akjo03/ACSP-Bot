package io.github.akjo03.discord.cscbot.util.commands.arguments;

import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CscCommandArgument<T, D extends CscBotCommandArgumentData<T>> {
	private final String name;
	private T value;
	private CscBotCommandArgumentData<T> data;

	public CscCommandArgument(String name, T value, D data) {
		this.name = name;
		this.value = value;
		this.data = data;
	}

	public Optional<CscBotConfigMessage> validate(ErrorMessageService errorMessageService) {
		if (data == null) {
			return Optional.empty();
		}
		if (value == null && data.getDefaultValue() != null) {
			value = data.getDefaultValue();
		}

		return data.validate(value, errorMessageService);
	}
}