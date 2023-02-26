package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;

import java.util.Optional;

public interface CscBotCommandArgumentData<T> {
	T getDefaultValue();
	Optional<CscBotConfigMessage> validate(T value, ErrorMessageService errorMessageService);
}