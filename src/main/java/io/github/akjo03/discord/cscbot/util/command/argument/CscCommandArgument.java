package io.github.akjo03.discord.cscbot.util.command.argument;

import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.lib.result.Result;
import lombok.Getter;

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

	public Result<Void> validate(ErrorMessageService errorMessageService) {
		if (data == null) {
			return Result.empty();
		}
		if (value == null && data.getDefaultValue() != null) {
			value = data.getDefaultValue();
		}

		return data.validate(value, errorMessageService);
	}
}