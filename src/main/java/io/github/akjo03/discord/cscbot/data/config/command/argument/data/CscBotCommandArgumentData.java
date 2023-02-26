package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.lib.result.Result;

import java.util.Optional;

public interface CscBotCommandArgumentData<T> {
	T getDefaultValue();
	Result<Void> validate(T value, ErrorMessageService errorMessageService);
}