package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import java.util.Optional;

public interface CscBotCommandArgumentData<T> {
	T getDefaultValue();
	Optional<String> validate(T value);
}