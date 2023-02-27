package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.lib.result.Result;

public interface CscBotCommandArgumentData<T> {
	T getDefaultValue();
	Result<T> parse(String value, ErrorMessageService errorMessageService);
}